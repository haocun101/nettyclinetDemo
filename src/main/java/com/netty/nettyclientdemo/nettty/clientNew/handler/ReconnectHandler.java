package com.netty.nettyclientdemo.nettty.clientNew.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;


public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private NettyClient tcpClient;

    public ReconnectHandler(NettyClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        log.info("客户端与服务端通道-开启：" );
        ctx.fireChannelActive();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        log.info("active close,no reconnect");
        Attribute<Boolean> attr = ctx.channel().attr(NettyClient.ACTIVE_CLOSE);
        Boolean activeClose = attr.get();
        if (activeClose != null && activeClose) {
//            log.info("active close,no reconnect");
            ctx.fireChannelInactive();
        } else {
            ctx.fireChannelInactive();
            tcpClient.reconnect();
        }
    }


}