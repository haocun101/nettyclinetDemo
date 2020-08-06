# nettyClient-demo


一、业务背景：
因业务量访问量很大，之前的socket的通信无法满足现在，在高并发的情况下，出现很多问题；
使用netty做client访问第三放服务，但是业务需求同步获取结果，还有第三方服务不能返回业务标识等；
所以只能用netty的同步.

二、方案：
1.利用java中的CountDownLatch类实现同步；
2.建立一次长连接，减少资源池的浪费；

三、结果

在200并发，持续10分钟，毫秒级返回数据；但是吞吐量不行；netty在同步时性能低；
后期加了commons-pool2连接池，用的是socket长连接之后，满足测试的要求。

