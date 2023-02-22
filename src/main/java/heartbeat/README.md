客户端：
1. 添加定时发起空闲事件的handler；
2. 添加接收空闲事件的handler；

服务端：
1. 接收心跳消息，响应心跳消息；


类功能说明：
- IdleStateHandler：定时发起idle事件；
- PingHandler：捕获空闲时间内未写事件，发出ping请求；
- RetryHandler：捕获空闲时间内未读事件，发起重新连接；
