package com.netty.nettyclientdemo.nettty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.CountDownLatch;


public class CustomerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private String response;
    private CountDownLatch countDownLatch;
    private byte[] param;

    private ClientChannelHandlerAdapter clientChannelHandlerAdapter;

    public CustomerChannelInitializer(byte[] param) {
        this.param = param;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        countDownLatch = new CountDownLatch(1);
        clientChannelHandlerAdapter = new ClientChannelHandlerAdapter(param, this);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(clientChannelHandlerAdapter);
    }

    public String getResponse() {
        try {
            if (countDownLatch == null) {
                countDownLatch = new CountDownLatch(1);
            }
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
        //释放线程锁
        countDownLatch.countDown();
    }

}
