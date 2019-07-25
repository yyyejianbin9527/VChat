package com.vchat.netty;/**
 * Created by Administrator on 2019/7/25.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName WSServer
 * @Description TODO
 * @Date 2019/7/25 14:26
 **/
public class WSServer {
    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    private static class WSServerSingleton{
        public static WSServer instance = new WSServer();
    }

    private WSServer(){
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap()
                .group(mainGroup,subGroup)
                .childHandler(new WSServerInitializer());
    }

    public WSServer getInstance(){
        return WSServerSingleton.instance;
    }
    public void start(){
        this.future = server.bind(8088);
        System.out.println("VChat netty server running!");
    }

    public void close(){
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
