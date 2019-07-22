package handler;/**
 * Created by Administrator on 2019/7/22.
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName CustomHandler
 * @Description TODO
 * @Date 2019/7/22 16:20
 **/
public class CustomHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 该处理器仅对 HTTP 请求响应
        if ( o instanceof HttpRequest) {
            // 获取当前的 channel
            Channel channel = channelHandlerContext.channel();

            // 显示客户端的远程地址
            System.out.println("customer ip address : "+channel.remoteAddress());

            // 定义发送的数据消息
            ByteBuf content = Unpooled.copiedBuffer("Hello netty", CharsetUtil.UTF_8);

            // 构建一个 http reponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,content);

            // 响应类型设置
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            // readableBytes() - 返回可读的字节长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            // 将响应发送到客户端
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
