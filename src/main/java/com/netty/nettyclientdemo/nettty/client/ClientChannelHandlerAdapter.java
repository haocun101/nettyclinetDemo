package com.netty.nettyclientdemo.nettty.client;

import com.netty.nettyclientdemo.util.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hc
 * @date 2020-07-27
 */
public class ClientChannelHandlerAdapter extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelHandlerAdapter.class);

    private byte[] param;
    private CustomerChannelInitializer customerChannelInitializer;

    public ClientChannelHandlerAdapter(byte[] param, CustomerChannelInitializer customerChannelInitializer) {
        this.param = param;
        this.customerChannelInitializer = customerChannelInitializer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        logger.info("客户端收到返回数据：" + ByteArrayUtil.bytesToHexString(req));
        customerChannelInitializer.setResponse(ByteArrayUtil.bytesToHexString(req));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端准备发送数据:" + ByteArrayUtil.bytesToHexString(param));
        ctx.writeAndFlush(Unpooled.copiedBuffer(param));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常");
        cause.printStackTrace();
        ctx.close();
    }

}
