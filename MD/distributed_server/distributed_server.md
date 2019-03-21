### 大纲
- 反代： nginx配置 lua 负载算法；底层（协议，集群，失效转移）
- 远程调用 dubbo：传输协议，序列化方式
- 消息队列 kafka：工作组，集群，持久化，发消息

### session 跨域解决方案
- session sticky: 同一客户端请求，落在同一服务器上
- session同步
- redis
- cookie：不安全，JSESSIONID
- token: 令牌

```
<artifactId>spring-session-data-redis</artifactId>
@EnableRedisHttpSession
```

### 分布式锁与幂等性

在分布式系统环境的复杂度、网络的不确定性会造成诸如时钟不一致、“拜占庭将军问题”（Byzantine failure）等。存在于集中式系统中的机器宕机、消息丢失等问题也会在分布式环境中变得更加复杂。

基于分布式系统的这些特征，有两种问题逐渐成为了分布式环境中需要重点关注和解决的典型：

- 互斥性: 对共享资源的抢占问题，操作的原子性
- 幂等性

在传统的基于数据库的架构中，对于数据的抢占问题往往是通过数据库事务（ACID）来保证的。

在分布式环境中，出于对性能以及一致性敏感度的要求，使得分布式锁成为了一种比较常见而高效的解决方案。

互斥锁：Lock（CAS+CLH队列） 和 synchronized（monitorenter和monitorexit）
- CAS：Compare and Swap
- CLH队列：带头结点的双向非循环链表

##### 多进程的解决方案

> 多进程中的临界资源大致上可以分为两类，一类是物理上的真实资源，如打印机；一类是硬盘或内存中的共享数据，如共享内存等

在多进程的情况下，主要还是利用操作系统层面的进程间通信原理来解决临界资源的抢占问题。比较常见的一种方法便是使用信号量（Semaphores）。

#### Redis 的分布式锁

[查看原文](https://mp.weixin.qq.com/s/hcl1Ijz0szXDs-7IRGunYQ)