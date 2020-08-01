package com.netty.nettyclientdemo.nettty.clientNew.util;



import ch.qos.logback.core.encoder.ByteArrayUtil;
import com.netty.nettyclientdemo.nettty.clientNew.handler.NettyClient;
import com.netty.nettyclientdemo.nettty.clientNew.pool.NettyClientPool;
import com.netty.nettyclientdemo.nettty.clientNew.pool.NettyClientPoolCacheByHostAndPort;
import com.netty.nettyclientdemo.nettty.clientNew.vo.HostAndPortConfig;
import org.apache.commons.lang3.time.StopWatch;

/**
 * http工具类，底层使用netty 长连接去发送请求。
 * 拿到要请求的url后，会提取出ip和端口，然后去commons pool查找对应的连接池，如果没有，则会初始化一个连接池
 * 假设连接池配置了最少要8个长连接，则连接池会初始化8个连接出来，（此时会使用netty去连接对应的服务），
 * 并返回1个可用连接。
 * 然后使用此连接，即可发送请求，并接收响应
 */

public class NettyHttpClientUtils {


    /**
     * body无需序列化为json，方法内部会进行序列化
     * @param url
     * @param body
     * @return
     */
    public static String sendData(String url,int port, byte[] body) {

        StopWatch timer = new StopWatch();
        timer.start();
        HostAndPortConfig config = new HostAndPortConfig(url,
                port);
        NettyClientPool nettyClientPool = NettyClientPoolCacheByHostAndPort.getOrCreateNettyClientPoolIfNecessay(config);
        NettyClient resource = nettyClientPool.getResource();
        if (resource == null) {
            throw new RuntimeException("获取连接失败");
        }
        timer.stop();
//        log.info("spent {} to get netty client",timer.getTime());

        try {
            timer.reset();
            timer.start();
            byte[] httpResponse = resource.doPost(url,port,body);
            if (httpResponse == null) {
                throw new RuntimeException("调用超时");
            }
            return ByteArrayUtil.toHexString(httpResponse);
        }finally {
            timer.stop();
//            log.info("spent {} to invoke rpc",timer.getTime());
            nettyClientPool.returnResourceObject(resource);
        }
    }
}
