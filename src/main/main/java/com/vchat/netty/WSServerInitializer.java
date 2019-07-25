package com.vchat.netty;/**
 * Created by Administrator on 2019/7/25.
 */

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Administrator
 * @version 1.0
 * @ClassName WSServerInitializer
 * @Description TODO
 * @Date 2019/7/25 14:26
 **/
public class WSServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // ========= 支持 HTTP  ============
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //===== 自定义 - 心跳检测 ===
        // 如果 1 分钟内没有发送读写心跳,则主动断开
        pipeline.addLast(new IdleStateHandler(2,4,6));
        pipeline.addLast(new HeartBeatHandler());
        // ======  支持 WebSocket ====
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 处理 WebSocket 文本格式数据
        pipeline.addLast(new ChatHandler());
    }
}
