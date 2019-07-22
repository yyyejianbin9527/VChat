package server;/**
 * Created by Administrator on 2019/7/22.
 */

import handler.HelloServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName HelloServer
 * @Description netty 服务器配置
 * @Date 2019/7/22 15:45
 **/
public class HelloServer {
    public static void main(String[] args) throws InterruptedException {
        // 使用 Netty 主从线程池模式

        // 定义一个主线程池,用于接收请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 定义一个从线程池,用于处理请求
        EventLoopGroup subGroup = new NioEventLoopGroup();

        // 创建启动服务器启动类实例
        ServerBootstrap bootstrap = new ServerBootstrap();

        try{
            // 服务器配置
            bootstrap.group(bossGroup,subGroup)                 // 服务器设置主从线程池
                    .channel(NioServerSocketChannel.class)      // 设置 NIO 双向通道,建立连接后创建的 Channel 实例类型
                    .childHandler(new HelloServerInitializer());// 助手类处理器配置

            // 启动 server,绑定端口,sync - 同步 等待端口绑定完毕。
            ChannelFuture channelFuture = bootstrap.bind(8088).sync();

            // 监听关闭的 channel,设置同步。
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
