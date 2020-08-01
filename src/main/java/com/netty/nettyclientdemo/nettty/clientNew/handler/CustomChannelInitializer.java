package com.netty.nettyclientdemo.nettty.clientNew.handler;


import com.netty.nettyclientdemo.nettty.clientNew.vo.HostAndPortConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class CustomChannelInitializer extends ChannelInitializer<Channel> {

    private final HostAndPortConfig config;

    private final NettyClient nettyClient;

    public CustomChannelInitializer(Bootstrap bootstrap,
                                    HostAndPortConfig config, NettyClient nettyClient) {
        super();
        this.config = config;
        this.nettyClient = nettyClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 用于处理大数据流
        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new ByteArrayEncoder());
        /**
         * 重连handler
         */
        pipeline.addLast(new ReconnectHandler(nettyClient));
        pipeline.addLast(new HttpResponseHandler());

    }


}