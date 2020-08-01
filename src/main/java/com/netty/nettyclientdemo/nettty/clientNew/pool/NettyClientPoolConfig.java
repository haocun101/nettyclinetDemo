package com.netty.nettyclientdemo.nettty.clientNew.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class NettyClientPoolConfig extends GenericObjectPoolConfig {

    public NettyClientPoolConfig() {
        setMaxIdle(500);
        setMaxWaitMillis(30000);
        setMinEvictableIdleTimeMillis(1800000);
        setMinIdle(50);
        setTestOnBorrow(false);
        setTestOnCreate(false);
        setTestOnReturn(false);
        setTestWhileIdle(true);
        setTimeBetweenEvictionRunsMillis(10000);
        setMaxTotal(1500);
        setNumTestsPerEvictionRun(1);
        setLifo(true);

    }

    public static final NettyClientPoolConfig DEFAULT = new NettyClientPoolConfig();
}
