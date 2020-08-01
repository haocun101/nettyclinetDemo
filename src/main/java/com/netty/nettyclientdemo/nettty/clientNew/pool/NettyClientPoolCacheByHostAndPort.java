package com.netty.nettyclientdemo.nettty.clientNew.pool;


import com.netty.nettyclientdemo.nettty.clientNew.vo.HostAndPortConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class NettyClientPoolCacheByHostAndPort {
    private static final Map<String, NettyClientPool> nodes = new HashMap<String, NettyClientPool>();
    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final Lock r = rwl.readLock();
    private static final Lock w = rwl.writeLock();

    public static NettyClientPool getOrCreateNettyClientPoolIfNecessay(HostAndPortConfig hostAndPortConfig,
                                                                       NettyClientPoolConfig nettyClientPoolConfig) {
        w.lock();
        try {
            String nodeKey = getNodeKey(hostAndPortConfig);
            NettyClientPool existingPool = nodes.get(nodeKey);
            if (existingPool != null){
                return existingPool;
            }

            NettyClientFactory factory = new NettyClientFactory(hostAndPortConfig);
            NettyClientPool nodePool = new NettyClientPool(nettyClientPoolConfig,factory);
            nodes.put(nodeKey, nodePool);
            return nodePool;
        } finally {
            w.unlock();
        }
    }
    /**
     * 创建连接池
     * @param hostAndPortConfig
     * @return
     */
    public static NettyClientPool getOrCreateNettyClientPoolIfNecessay(HostAndPortConfig hostAndPortConfig) {
        return getOrCreateNettyClientPoolIfNecessay(hostAndPortConfig, NettyClientPoolConfig.DEFAULT);
    }


    public static String getNodeKey(HostAndPortConfig hnp) {
        return hnp.getHost() + ":" + hnp.getPort();
    }


}
