package websocket;/**
 * Created by Administrator on 2019/7/22.
 */


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName WSServerInitializer
 * @Description TODO
 * @Date 2019/7/22 17:38
 **/
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // websocket 基于 HTTP 协议,需要使用 HTTP 编解码器
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // HttpMessage 进行聚合,聚合成 FullHttpRequest 或 FullHttpResponse
        // 几乎在 Netty 中的编程都会使用到此 Handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        // ======== 以上用于支持 HTTP 协议 =========

        /*
        * websocket 服务器处理的协议,用于指定客户端连接访问的路由：/ws
        *
        * 处理握手动作：handshaking(close、ping、pong) ping + pong = 心跳
        *
        * 对于 websocket ,都是以 frames 进行传输,不同的数据类型对应的 frames 也不同
        * */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new ChatHandler());
    }
}
