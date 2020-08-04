package com.netty.nettyclientdemo.socket;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketConnectionFactory extends BasePooledObjectFactory<Socket>
{
    private InetSocketAddress socketAddress = null;

    public SocketConnectionFactory(String hosts, int port)
    {
        this.socketAddress = new InetSocketAddress(hosts, port);
    }
    @Override
    public void destroyObject(PooledObject<Socket> p) throws Exception
    {
        Socket socket = (Socket)p.getObject();
        if (socket != null)
            socket.close();
    }

    @Override
    public boolean validateObject(PooledObject<Socket> p) {
        Socket socket = p.getObject();
        if(socket != null) {
            if (!socket.isConnected()) {
                return false;
            }
            if (socket.isClosed()) {
                return false;
            }
        }
        return false;
    }
    @Override
    public Socket create() throws Exception {
        Socket socket = new Socket();
        socket.connect(this.socketAddress, 50000);
        socket.setReuseAddress(true);
        return socket;
    }
    @Override
    public PooledObject<Socket> wrap(Socket obj) {
        return new DefaultPooledObject(obj);
    }
}