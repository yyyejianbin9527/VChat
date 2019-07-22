package handler;/**
 * Created by Administrator on 2019/7/22.
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * @author Administrator
 * @version 1.0
 * @ClassName HelloServerInitializer
 * @Description 初始化器,当客户端与服务器建立连接后（注册 channel）,会执行该类里面相对应的初始化方法
 * @Date 2019/7/22 16:06
 **/
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 通过 channel 获取对应的 Pipeline 管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 通过管道添加 handler
        pipeline.addLast("HttpServerCodec",new HttpServerCodec()); // Http 连接的编解码

        // 自定义助手类,功能 - 返回 "hello netty"
        pipeline.addLast("customeHandler",new CustomHandler());
    }
}
