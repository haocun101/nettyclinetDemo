package com.netty.nettyclientdemo.nettty.clientNew.handler;


import com.netty.nettyclientdemo.util.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

/**
 * http请求响应的处理器
 */
public class HttpResponseHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf fullHttpResponse) throws Exception {

        String result = ByteBufUtil.hexDump(fullHttpResponse);
//        log.info("返回数据"+result);
        Promise<byte[]> promise= (Promise<byte[]>) ctx.channel().attr(NettyClient.CURRENT_REQ_BOUND_WITH_THE_CHANNEL).get();
        promise.setSuccess(ByteArrayUtil.hexString2Bytes(result));
    }


}
