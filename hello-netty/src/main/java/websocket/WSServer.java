package websocket;/**
 * Created by Administrator on 2019/7/22.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName WSServer
 * @Description TODO
 * @Date 2019/7/22 17:26
 **/
public class WSServer {
    public static void main(String[] args) throws InterruptedException {
        // 使用 Netty 主从线程池模式

        // 1. 创建主从线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();

        // 2. 配置启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());

        try{
            // 3. 开启服务
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            // 监听 channel 关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
