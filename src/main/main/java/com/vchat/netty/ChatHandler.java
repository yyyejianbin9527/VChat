package com.vchat.netty;/**
 * Created by Administrator on 2019/7/25.
 */


import com.vchat.SpringUtil;
import com.vchat.enums.MsgActionEnum;
import com.vchat.pojo.ChatMsg;
import com.vchat.service.IUsersService;
import com.vchat.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName ChatHandler
 * @Description TODO
 * @Date 2019/7/25 14:26
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel currentChannel = ctx.channel();

        DataContent dataContent = JsonUtils.jsonToPojo(content,DataContent.class);
        Integer action = dataContent.getAction();
        // todo 重构
        if (action == MsgActionEnum.CONNECT.getType()) {
            String senderId = dataContent.getChatMsg().getSendUserId();
            // 将用户 id 与 用户当前的 channel 连接起来
            UserChannelRel.put(senderId,currentChannel);
        } else if (action == MsgActionEnum.CHAT.getType()) {
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgText = chatMsg.getMsg();
            String receiverId = chatMsg.getAcceptUserId();
            String senderId = chatMsg.getSendUserId();

            // 将聊天记录保存到数据库,并记录消息的状态 - 未接收
            IUsersService usersService = (IUsersService) SpringUtil.getBean("userService");
            String msgId = usersService.saveMsg(chatMsg);
            chatMsg.setId(msgId);

            // todo,可能出现问题。ChatMsg
            // 如果接收消息者没有连接服务器怎么办?
            Channel acceptChannel = UserChannelRel.get(chatMsg.getAcceptUserId());

            if (acceptChannel == null) {
                // 用户离线,需要推送
            } else {
                Channel findAcceptChannel = users.find(acceptChannel.id());

                if (findAcceptChannel != null) {
                    // 用户在线
                    acceptChannel.writeAndFlush(new TextWebSocketFrame(
                            JsonUtils.objectToJson(chatMsg)));
                } else {
                    // 用户离线,需要推送
                }
            }

        } else if (action == MsgActionEnum.SIGNED.getType()) {
//  2.3  签收消息类型，针对具体的消息进行签收，修改数据库中对应消息的签收状态[已签收]
            IUsersService userService = (IUsersService)SpringUtil.getBean("userServiceImpl");
            // 扩展字段在signed类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdsStr = dataContent.getExtand();
            String msgIds[] = msgIdsStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String mid : msgIds) {
                if (StringUtils.isNotBlank(mid)) {
                    msgIdList.add(mid);
                }
            }

            System.out.println(msgIdList.toString());

            if (msgIdList != null && !msgIdList.isEmpty() && msgIdList.size() > 0) {
                // 批量签收
                userService.updateMsgSigned(msgIdList);
            }
        } else if (action == MsgActionEnum.KEEPALIVE.getType()) {

        } else if (action == MsgActionEnum.PULL_FRIEND.getType()) {

        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asShortText();
        System.out.println("客户端被移除，channelId为：" + channelId);

        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        users.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
