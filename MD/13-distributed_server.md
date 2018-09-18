消息重发

有序性，丢失，kafka

消息重复消费，幂等性

#### session 跨域解决方案
- session sticky: 同一客户端请求，落在同一服务器上
- session同步
- redis
- cookie：不安全
- token: 令牌

服务端根据Cookie中的JSESSIONID取到了对应的session

<artifactId>spring-session-data-redis</artifactId>
@EnableRedisHttpSession

#### Nginx
master进程，worker

负载：随机，轮询，权重，iphash

高可用：keepalived

### 分布式相关
- [分布式限流](http://crossoverjie.top/2018/04/28/sbc/sbc7-Distributed-Limit/)
- [基于 Redis 的分布式锁](http://crossoverjie.top/2018/03/29/distributed-lock/distributed-lock-redis/)
- [分布式缓存设计](https://github.com/crossoverJie/JCSprout/blob/master/MD/Cache-design.md)
- [分布式 ID 生成器](https://github.com/crossoverJie/JCSprout/blob/master/MD/ID-generator.md)

### 常用框架

- [Spring Bean 生命周期](https://github.com/crossoverJie/JCSprout/blob/master/MD/spring/spring-bean-lifecycle.md)
- [Spring AOP 的实现原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/SpringAOP.md)
- [Guava 源码分析（Cache 原理）](https://crossoverjie.top/2018/06/13/guava/guava-cache/)

### 架构设计
- [秒杀系统设计](https://github.com/crossoverJie/JCSprout/blob/master/MD/Spike.md)
- [秒杀架构实践](http://crossoverjie.top/2018/05/07/ssm/SSM18-seconds-kill/)

### DB 相关

- [MySQL 索引原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/MySQL-Index.md)
- [SQL 优化](https://github.com/crossoverJie/JCSprout/blob/master/MD/SQL-optimization.md)
- [数据库水平垂直拆分](https://github.com/crossoverJie/JCSprout/blob/master/MD/DB-split.md)

### 数据结构与算法
- [红包算法](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/red/RedPacket.java)
- [二叉树层序遍历](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/BinaryNode.java#L76-L101)
- [是否为快乐数字](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/HappyNum.java#L38-L55)
- [链表是否有环](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/LinkLoop.java#L32-L59)
- [从一个数组中返回两个值相加等于目标值的下标](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/TwoSum.java#L38-L59)
- [一致性 Hash 算法](https://github.com/crossoverJie/JCSprout/blob/master/MD/Consistent-Hash.md)
- [限流算法](https://github.com/crossoverJie/JCSprout/blob/master/MD/Limiting.md)
- [三种方式反向打印单向链表](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/ReverseNode.java)
- [合并两个排好序的链表](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/MergeTwoSortedLists.java)
- [两个栈实现队列](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/TwoStackQueue.java)
- [动手实现一个 LRU cache](http://crossoverjie.top/2018/04/07/algorithm/LRU-cache/)
- [链表排序](./src/main/java/com/crossoverjie/algorithm/LinkedListMergeSort.java)
- [数组右移 k 次](./src/main/java/com/crossoverjie/algorithm/ArrayKShift.java)

### Netty 相关
- [SpringBoot 整合长连接心跳机制](https://crossoverjie.top/2018/05/24/netty/Netty(1)TCP-Heartbeat/)
- [从线程模型的角度看 Netty 为什么是高性能的？](https://crossoverjie.top/2018/07/04/netty/Netty(2)Thread-model/)

### 附加技能

- [TCP/IP 协议](https://github.com/crossoverJie/JCSprout/blob/master/MD/TCP-IP.md)
- [一个学渣的阿里之路](https://crossoverjie.top/2018/06/21/personal/Interview-experience/)
