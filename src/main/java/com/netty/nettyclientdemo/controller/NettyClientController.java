package com.netty.nettyclientdemo.controller;

import com.netty.nettyclientdemo.nettty.client.NettyClient;
import com.netty.nettyclientdemo.util.ByteArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: nettyclinetdemo
 * @ClassName: NettyClientController
 * @description: 测试
 * @author: hc
 * @create: 2020-07-27 13:11
 **/
@RestController
public class NettyClientController {

    @Autowired
    NettyClient nettyClient;

    @RequestMapping("/netty/test")
    public String test(String param) {
        return nettyClient.sendData(ByteArrayUtil.hexString2Bytes(param));
    }

}
