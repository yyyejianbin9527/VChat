package websocket;/**
 * Created by Administrator on 2019/7/22.
 */

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName ChatHandler
 * @Description TODO
 * @Date 2019/7/22 17:54
 **/
/*
*
* TextWebSocketFrame  - 在 Netty 中,是用于为 websocket 专门处理文本的对象,frame 是消息的载体。
* */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的 channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        System.out.println("接收到的数据：" + content);

        for (Channel channel : clients) {
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]"+
                    new Date().toString()+",消息："+content));
        }

//        clients.writeAndFlush(); -  与 forEach 一致
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 当客户端连接服务器之后,获取客户端的 channel 并放到 channelGroup 容器中进行管理
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发 handlerRemoved,channelGroup 会自动移除对应客户端的 channel,因此不需要维护 clients
        // clients.remove(ctx.channel());
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(ctx.channel().id().asShortText());
    }
}
