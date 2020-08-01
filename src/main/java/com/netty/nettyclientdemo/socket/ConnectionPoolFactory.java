package com.netty.nettyclientdemo.socket;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Socket;

@Component
public class ConnectionPoolFactory {

    @Value("${face.ip}")
    private String ip;

    @Value("${face.port}")
    private int port;

    @Value("${socket.lifo}")
    private boolean lifo;

    @Value("${socket.maxTotal}")
    private int maxTotal;

    @Value("${socket.maxIdle}")
    private int maxIdle;

    @Value("${socket.maxWait}")
    private int maxWait;

    @Value("${socket.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${socket.minIdle}")
    private int minIdle;

    @Value("${socket.numTestsPerEvictionRun}")
    private int numTestsPerEvictionRun;

    @Value("${socket.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${socket.testOnReturn}")
    private boolean testOnReturn;

    @Value("${socket.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${socket.testOnCreate}")
    private boolean testOnCreate;

    @Value("${socket.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    private GenericObjectPool<Socket> pool;

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(this.maxIdle);
        config.setMaxWaitMillis(this.maxWait);
        config.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
        config.setMinIdle(this.minIdle);

        config.setTestOnBorrow(this.testOnBorrow);
        config.setTestOnCreate(this.testOnCreate);
        config.setTestOnReturn(this.testOnReturn);
        config.setTestWhileIdle(this.testWhileIdle);
        config.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        config.setMaxTotal(this.maxTotal);
        config.setNumTestsPerEvictionRun(this.numTestsPerEvictionRun);
        config.setLifo(this.lifo);

        SocketConnectionFactory factory = new SocketConnectionFactory(this.ip, this.port);
        this.pool = new GenericObjectPool(factory, config);
    }

    public Socket getConnection() throws Exception {
        return this.pool.borrowObject();
    }

    public void releaseConnection(Socket socket) {
        try {
            this.pool.returnObject(socket);
        } catch (Throwable e) {
            if (socket != null)
                try {
                    socket.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
        }
    }
}