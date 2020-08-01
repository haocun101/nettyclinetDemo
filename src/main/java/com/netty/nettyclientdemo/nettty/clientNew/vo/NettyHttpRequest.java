package com.netty.nettyclientdemo.nettty.clientNew.vo;

/**
 * http + json
 */

public class NettyHttpRequest {
    private byte[] body;
    private String uri;
    int port;
    public NettyHttpRequest(String uri,int port , byte[] body) {
        this.body = body;
        this.port = port;
        this.uri = uri;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
