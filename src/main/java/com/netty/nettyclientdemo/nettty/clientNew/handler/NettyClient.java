package com.netty.nettyclientdemo.nettty.clientNew.handler;




import com.netty.nettyclientdemo.nettty.clientNew.util.NamedThreadFactory;
import com.netty.nettyclientdemo.nettty.clientNew.util.NettyEventLoopFactory;
import com.netty.nettyclientdemo.nettty.clientNew.vo.HostAndPortConfig;
import com.netty.nettyclientdemo.nettty.clientNew.vo.NettyHttpRequest;
import com.netty.nettyclientdemo.util.ByteArrayUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class NettyClient implements Closeable {
    /**
     * netty client bootstrap
     */
    private static final EventLoopGroup NIO_EVENT_LOOP_GROUP = NettyEventLoopFactory.eventLoopGroup(20, "NettyClientWorker");

    /**
     * netty client bootstrap
     */
    private static final DefaultEventLoop NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP =  new DefaultEventLoop(null, new NamedThreadFactory("NettyResponsePromiseNotify"));



    private HostAndPortConfig config;

    /**
     * 当前使用的channel
     */
    Channel channel;

    /**
     * 重连的future
     */
    ScheduledFuture<?> scheduledFuture;


    public boolean isbIsConnectionOk() {
        return bIsConnectionOk;
    }

    boolean bIsConnectionOk;


    public NettyClient(HostAndPortConfig config) {
        this.config = config;

    }

    public void initConnection() {
//        log.info("initConnection starts...");

        Bootstrap bootstrap;
        bootstrap = createBootstrap(config);
        ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
//        log.info("current thread:{}", Thread.currentThread().getName());
        boolean ret = future.awaitUninterruptibly(2000, MILLISECONDS);

        boolean bIsSuccess = ret && future.isSuccess();
        if (!bIsSuccess) {
            bIsConnectionOk = false;
//            log.error("host config:{}",config);
            throw new RuntimeException("连接失败");
        }

        cleanOldChannelAndCancelReconnect(future, channel);

        bIsConnectionOk = true;
    }

    private void cleanOldChannelAndCancelReconnect(ChannelFuture future, Channel oldChannel) {
        /**
         * 连接成功，关闭旧的channel，再用新的channel赋值给field
         */
        try {
            if (oldChannel != null) {
                try {
//                    log.info("Close old netty channel " + oldChannel);
                    oldChannel.close();
                } catch (Exception e) {
                    e.printStackTrace();
//                    log.error("e:{}", e);
                }
            }
        } finally {
            /**
             * 新channel覆盖field
             */
            NettyClient.this.channel = future.channel();
            NettyClient.this.bIsConnectionOk = true;
//            log.info("connection is ok,new channel:{}", NettyClient.this.channel);
            if (NettyClient.this.scheduledFuture != null) {
//                log.info("cancel scheduledFuture");
                NettyClient.this.scheduledFuture.cancel(true);
            }
        }
    }


    private Bootstrap createBootstrap(HostAndPortConfig config) {
        Bootstrap bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(NIO_EVENT_LOOP_GROUP);

        bootstrap.handler(new CustomChannelInitializer(bootstrap, config, this));
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
//        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        return bootstrap;
    }

    public static final AttributeKey< Promise<byte[]>> CURRENT_REQ_BOUND_WITH_THE_CHANNEL =
            AttributeKey.valueOf("CURRENT_REQ_BOUND_WITH_THE_CHANNEL");

    public byte[] doPost(String url,int port, byte[] body) {
        NettyHttpRequest request = new NettyHttpRequest(url,port, body);
        return doHttpRequest(request);
    }


    private byte[] doHttpRequest(NettyHttpRequest request) {
        Promise<byte[]> defaultPromise = NETTY_RESPONSE_PROMISE_NOTIFY_EVENT_LOOP.newPromise();
        channel.attr(CURRENT_REQ_BOUND_WITH_THE_CHANNEL).set(defaultPromise);
        byte[] body =(byte[]) request.getBody();
        byte[] headLen = new byte[2];
        byte[] command1 = null;
        byte[] command2 = null;
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        buffer.put(body);
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
//        log.info("发送到HSM报文：\n" + ByteArrayUtil.toHexString(command2));

        ChannelFuture channelFuture = channel.writeAndFlush(command2);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
//                    log.info(Thread.currentThread().getName() + " 请求发送完成");
                }else {
//                    log.info(Thread.currentThread().getName() + " 请求发送失败");
                }

            }
        });
        return get(defaultPromise);
    }


    public <V> V get(Promise<V> future) {
        if (!future.isDone()) {
            final CountDownLatch l = new CountDownLatch(1);
            future.addListener(new GenericFutureListener<Future<? super V>>() {
                @Override
                public void operationComplete(Future<? super V> future) throws Exception {
//                    log.info("received response,listener is invoked");
                    if (future.isDone()) {
                        // io线程会回调该listener
                        l.countDown();
                    }
                }
            });

            boolean interrupted = false;
            if (!future.isDone()) {
                try {
                    l.await(4, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
//                    log.error("e:{}", e);
                    interrupted = true;
                }

            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }

        if (future.isSuccess()) {
            return future.getNow();
        }
//        log.error("wait result time out ");
        return null;
    }


    public void reconnect() {
//        log.info("start to reconnect,current thread:{}", Thread.currentThread().getName());
        Bootstrap bootstrap = createBootstrap(config);
        ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                ChannelFuture channelFuture = (ChannelFuture) future;
                if (future.isSuccess()) {
                    /**
                     * 清理旧的channel和取消重连future
                     */
                    NettyClient.this.cleanOldChannelAndCancelReconnect(channelFuture,
                            NettyClient.this.channel);
                    return;
                }

                scheduledFuture = channelFuture.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        reconnect();
                    }
                }, 2, TimeUnit.SECONDS);
//                log.info("schedule a task to reconnect");
            }
        });
    }


    public static AttributeKey<Boolean> ACTIVE_CLOSE = AttributeKey.valueOf("ACTIVE_CLOSE");

    @Override
    public void close() throws IOException {
        if (channel != null) {
            channel.attr(ACTIVE_CLOSE).set(true);
            channel.close().syncUninterruptibly();
            bIsConnectionOk = false;
        }

        if (scheduledFuture != null) {
//            log.info("netty client is going to close,cancel scheduledFuture");
            scheduledFuture.cancel(true);
        }
    }

}
