package com.netty.nettyclientdemo.nettty.client;

import com.netty.nettyclientdemo.util.ByteArrayUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


@Component
public class NettyClient {

    @Value("${netty.ip}")
    private String ip;

    @Value("${netty.port}")
    private int port;

    static Bootstrap bootstrap;
    static NioEventLoopGroup worker;

    @PostConstruct
    public void init() {
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .remoteAddress(new InetSocketAddress(ip, port));
    }

    public String sendData(byte[] param) {
        byte[] data = getData(param);
        CustomerChannelInitializer customerChannelInitializer = new CustomerChannelInitializer(data);
        bootstrap.handler(customerChannelInitializer);
        ChannelFuture channelFuture = null;
        String msg = "";
        try {
            channelFuture = bootstrap.connect().sync();
            return customerChannelInitializer.getResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != channelFuture) {
                channelFuture.channel().close();
            }

        }
        return null;
    }

    /**
     * 组装数据
     *
     * @param data
     * @return
     */
    public byte[] getData(byte[] data) {
        byte[] headLen = new byte[2];
        byte[] command1 = null;
        byte[] command2 = null;
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.put(data);
        buffer.flip();

        byte[] command = new byte[buffer.limit()];
        buffer.get(command);
        buffer.clear();
        command1 = new byte[command.length];
        System.arraycopy(command, 0, command1, 0, command.length);
        headLen = ByteArrayUtil.BigEndian.toByteArray(command1.length, 2);
        command2 = new byte[headLen.length + command1.length];

        System.arraycopy(headLen, 0, command2, 0, headLen.length);
        System.arraycopy(command1, 0, command2, headLen.length, command1.length);
        return command2;
    }

}
