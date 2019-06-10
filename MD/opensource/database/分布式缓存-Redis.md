#### 基本用途
- String，用作缓存系统
- 使用 SetNx 可以实现简单的分布式锁
- 使用 list 的 Pop 和 Push 功能可以作为阻塞队列/非阻塞队列
- 使用 SUBSCRIBE 和 PUBLISH 可以实现发布/订阅模型
- 对数据进行实时分析，如可以累加统计等
- 使用 Set 做去重的计数统计
- 使用 SortedSet 可以做排行榜等排序场景
- 使用 getbit、 setbit、 bitcount 做大数据量的去重统计，在允许误差的情况下可使用 HyperLogLog 
- 使用 GEO 可以实现位置定位、附近的人

以上场景基本上涵盖了 Redis 支持的各种存储结构


#### 数据类型
- Key：可以是任意类型，但最终都会存储为 byte[]
- String：简单的（key, value）存储结构，支持数据的自增、支持 BitSet 结构
- Hash：哈希表数据结构，支持对 field 的自增等操作
- list：列表，支持按照索引、索引范围获取元素以及 Pop、 Push 等堆栈操作
- Set：集合，去重的列表
- SortedSet：有序集合
- Hyperloglog：可对大数据进行去重，有一定的误差率
- GEO：地理位置的存储结构，支持 GEOHASH


#### 内存压缩
Redis 的存储是以内存为主的，因此如何节省内存是非常关键的地方。

首先，key越短越好，可以采取编码或者简写的方式。 如用户的笔记数目 key 可以使用 `u:{uid}:n_count`。同时，key 的数量也要控制，可以考虑使用 hash 做二级存储来合并类似的 key 从而减少 key 的数量。

其次，value 也是越小越好，尤其是在存储序列化后的字节时，要选择最节省内存的序列化方式，如 Kryo、 Protobuf等。

此外， Redis 支持的数据结构的底层实现会对内存使用有很大的影响，如在缓存用户的头像时，可以根据用户ID 做分段存储，每一段使用 hash 结构进行存储：
```java
// 第 1 段 l~999
hset u:avatar:l 1 http://xx
hset u:avatar:l 2 http://xx
// 第 2 段 1000~1999
hset u:avatar:2 1000 http://xx
hset u:avatar:2 1999 http://xx
```

这样，相比使用 String 存储， hash 底层会使用 ZipList，极大地节省了内存。但这里需要注意的是， Redis 有一个 `hash-max-ziplist-entries` 参数，默认值 512，如果 hash 中的 field 数目超过此值， 那么 hash 将开始使用 HashTable。 但是，此值设置过大，在查询的时候就会变慢。 此外，还有一个 `hash-max-ziplist-value` 参数，默认是 64 字节， value 的最大字符串字节大小如果大于此值，则不会使用 ZipList。

Hash对象只有同时满足下面两个条件时，才会使用ziplist（压缩列表):
1. 哈希中元素数量小于512个
2. 哈希中所有键值对的键和值字符串长度都小于64字节

此外，对于 list 来说， Redis 3.2 使用了新的数据结构 quicklist 来编码实现，废弃了 list-mix-ziplist-value 和 list-max-ziplist-entries 配置，使用 `list-max-ziplist-size` （负数表示最大占用空间，正数表示最大压缩长度）和 `list-compress-depth` （最大压缩深度）这两个参数进行配置。


#### Redis Lua 
一般情况下， Redis 提供的各种操作命令已经能够满足我们的需求。 如果需要一次将多个操作请求发送到服务器端，可以通过 Jedis客户端的 pipeline 接口批量执行。 但如果有以下 3种需求，就需要使用 Lua：
- 需要保证这些命令作为一个整体的原子性
- 这些命令之间有依赖关系
- 业务逻辑除了 Redis 操作外还包括其他逻辑运算

Redis 从 2.6版本后内置对 Lua Script 的支持，通过 eval 或者 evalsha 执行 Lua 脚本。其脚本的执行具有原子性，因此适用于秒杀、签到等需要并发互斥且有一些业务逻辑的业务场景。 如下：
```java
String REDIS_SCRIPT_GRAB_GIFT = "local giftLeft = tonumber(redis.call ('get', KEYS[l] )) or 0;" // 读取礼物剩余数量
	+ "if(giftLeft <= 0) then return 0; end；" // 抢购失败
	+ "redis.call('deer', KEYS[1]);" // 减少礼物数量
	+ "return l;";
... 
Object grabResult = jedis.eval(REDIS_SCRIPT_GRAB_GIFT, Lists.newArrayList("test:gifts:" + giftid + ": left"), null); 
```

使用 Redis Lua 需要注意以下几点:
- Lua 脚本里涉及的所有 key 尽量用变量，从外面传入，使 Redis 一开始就知道你要改变哪些 key，尤其是在使用 Redis 集群的时候。
- 建议先用 SCRIPT LOAD 载入 script，返回哈希值。然后用 EVALHASH 执行脚本，这样可以节省脚本传输的成本。
- 如果想从 Lua 返回一个浮点数，应该将它作为一个字符串（比如 ZSCORE命令）。因为 Lua 中整数和浮点数之间没有什么区别，在返回浮点数据类型时会转换为整数。


#### 数据失效和淘汰机制

如果某些数据并不需要永远存在，可以通过 Expire 设置其失效时间，让其在这段时间后被删除。 这里设置了失效时间之后，可以通过 SET 和 GETSET 命令覆写失效期，或者使用 PERSIST去掉失效期。需要注意的是，如果一个命令只是更新一个带生存时间的 key 的值，而不是用一个新的 key 值来代替它的话，那么生存时间不会被改变。 如 INCR、 DECR、 LPUSH、 HSET 等命令。此外，设置了失效期的 key 其 TTL 是大于 0 的，直至被删除会变为－2，未设置失效期的 key 其 TTL 为－1。

和大部分缓存一样，过期数据并非是立即被删除的:
- 消极方法：get 或 set 时触发失效删除

- 积极方法：后台线程周期性（每 100毫秒）随机选取 100 个设置了有效期的 key 进行失效删除，如果有 1/4 的 key 失效，那么立即再选取 100 个设置了有效期的 key 进行失效删除。

当使用主从模式时，删除操作只在 Master 端做，在 Slave 端是无效的。

此外，当内存使用达到 maxmemory 后， 会触发缓存淘汰。 Redis 支持以下几种淘汰策略：
- volatile-lru：从已设置过期时间的数据集中挑选最近最少使用的数据淘汰，是 Redis 默认的淘汰策略
- volatile-ttl：从已设置过期时间的数据集中挑选将要过期的数据淘汰
- volatile-random：从已设置过期时间的数据集中任意选择数据淘汰

- allkeys-lru：从数据集中挑选最近最少使用的数据淘汰
- allkeys-random：从数据集中任意选择数据淘汰

- no-enviction：禁止驱逐数据

Redis 为了节省内存占用使用了整数对象池（即共享整数对象），但当淘汰策略为 LRU 时，由于无法对对象池的同一个对象设置多个访问时间戳，因此不会再使用整数对象池。


#### 数据存储机制
Redis 支持对内存中的数据进行持久化，包括两种实现方式：

- 1) RDB 
RDB 是基于二进制快照的持久化方案 ，其在指定的时间间隔内（默认触发策略是 60秒内改了 l万次或 300秒内改了 10次或 900秒内改了 1次）生成数据集的时间点快照（Point-In-Time Snapshot），从而实现持久化。

基于快照的特性， 会使其丢失一些数据，比较适用于对 Redis 的数据进行备份。 

RDB 进行时， Redis 会 fork() 出一个子进程来遍历内存中的所有数据来进行持久化。当数据集比较庞大时，这个过程会非常耗时， 会造成服务器停止处理客户端，停止时间可能会长达 1 秒。

可配置 RDB 对数据进行压缩存储，支持字符串的 LZF 算法和 string 形式的数字变回 int 形式。

- 2) AOF 

AOF 是基于日志的持久化方案，记录服务器执行的所有写操作命令，并在服务器启动时，通过重新执行这些命令来还原数据集。 这些命令全部以 Redis 协议的格式来保存（纯文本文件），新命令会被追加到文件的末尾。

为了避免 AOF 的文件体积超出保存数据集状态所需的实际大小，Redis 在 AOF 文件过大时会 fork 出一个子进程对 AOF 文件进行重写。 AOF 这种方案，默认是每隔 l 秒进行一次 fsync（将日志写入磁盘），因此与 RDB相比， 其最多丢失 1 秒的数据，当然如果配置成每次执行写入命令时 fsync（非常慢），甚至可以避免任何数据的丢失。

但其文件的体积明显大于 RDB，将日志刷到磁盘和从 AOF 恢复数据的过程也是慢于 RDB 的。
如果想要保证数据的安全性，建议同时开启 AOF 和 RDB，此时由于 RDB 有可能丢失文件， Redis 重启时会优先使用 AOF 进行数据恢复。

此外，可以通过 save 或者 bgsave 命令来手动触发 RDB 持久化，通过 bgrewriteaof触发 AOF 重写。 如此可以将 RDB 或 AOF 文件传到另一个 Redis 节点进行数据迁移。

需要注意的是，如果通过 kill -9 或者 Ctrl+C 来关闭 Redis，那么 RDB 和 AOF 都不会被 触发，这样会造成数据丢失，建议使用 redis-cli shutdown 或者 kill 优雅关闭 Redis。


#### 分布式
Redis 本身对分布式的支持有如下这 3 种:

- 1) Master-Slave 

简单的主从模式，通过执行 slaveof命令来启动，一旦执行， Slave 会清掉自己的所有数据，同时 Master 会 bgsave 出一个 RDB 文件并以 Client 的方式连接 Slave 发送写命令给 Slave 传输数据。

Redis 还提供了 Redis Sentinel 做这种方案的 fail-over，能够对 Redis 主从复制进行监控，并实现主挂掉之后的自动故障转移。

首先，Sentinel 会在 Master 上建一个 pub/sub Channel，通告各种信息。 所有 Sentinel 通过接收 pub/sub Channel 上的 +Sentinel 的信息发现彼此（Sentinel 每 5 秒会发送一次 _sentinel_：hello 消息）。

然后， Sentinel 每秒会对所有 Master、Slave 和其他 Sentinel 执行 `ping`，这些 Redis-Server 会响应 +PONG、-LOADING 或 -MASTERDOWN 告知其存活状态等。 如果一台 Sentinel 在 30秒内没有收到 Master 的应答，会认为 Master 已经处于 SDOWN 状态，同时会询问其他 Sentinel 此 Master 是否 SDOWN，如果 quonum 台 Sentinel 认为 Master 已经 SDOWN，那么就认为 Master 是真的挂掉（ODOWN），此时会选出一个状态正常且与 Master 的连接没有断开太久的 Slave 作为新的 Master。

Redis Sentinel 提供了 notify脚本机制可以接收任何 pub/sub 消息，以便于发出故障告警等信息；提供了 reconfig 脚本机制在 Slave 开始提升成 Master、所有Slave 都已指向新 Master、 提升被终止等情况下触发对此类脚本的调用，可以实现一些自定义的配置逻辑。

- 2) Redis Cluster

Redis3.0 后内置的集群方案。 没有中心节点，每一个 Redis 实例都负责一部分 slot（存储一部分key），业务应用需要通过 Redis Cluster 客户端程序对数据进行操作。客户端可以向任一实例发出请求，如果所需数据不在该实例中， 则该实例引导客户端去对应实例读/写数据。 Redis Cluster 的成员管理（节点名称、IP、端口、状态、角色）等，都通过节点之间两两通信、定期交换并更新。这是一种比较重的集群方案。

很多公司都采用基于代理中间件的思路做了一些实现， Twemproxy、 Codis 是其中用得比较多的。相比官方的集群方案，其使用方式和单点 Redis 一模一样，原有的业务改动很少（个别命令会不被支持），且其数据存储和分布式逻辑是分离的，便于扩展和升级。


#### 使用提示
1) Redis 数据操作

- 不要在大数据量线上环境中使用 keys 命令，这很容易造成 Redis 阻塞

- 尽量使用 mset、 hmset 等批量操作，以节省网络 I/O

- 使用 pipeline 一次执行多条相互没有依赖关系的命令可以节省网络 I/O 成本，但pipeline和事务不同，其只是一种批量写/读的多命令流水线机制，Redis服务器并不保证这些命令的原子性

- 使用 Redis 的事务命令（multi 、 exec、 discard），其事务级别类似于 Read Committed，即事务无法看到其他事务未提交的改动。 还可以使用 watch 对某一个 key 做监控，当 key 对应的值被改变时，事务会被打断，能够达到 CAS 的效果

-  使用 list 做队列时，如果需要 ACK，可以考虑再使用一个 SortedSet，每次队列中 Pop 出一个元素则按照访问时间将其存储到 SortedSet 中，消费完后进行删除

-  对大集合键数据的删除（元素非常多的 hash、 list、 set、 sortedSet）避免使用 del, 会造成 Redis 阻塞

- hash：通过 hscan，每次获取一部分字段，再用 hdel命令，每次删除一个字段

- list：使用 ltrim 每次删除少量元素
- set: 使用 sscan，每次扫描集合中一部分元素，再用 srem每次删除一个键
- sortedSet：使用 zremrangebyrank，每次删除顶部 100 个元素

- 在 Java 开发中一般选择直接使用 Jedis 即可。 如果需要诸如分布式锁、主从等分布式特性或者应用层级的 Redis 操作封装，可以选择使用 Redisson 库来操作 Redis

2）配置与监控

- 可以通过 monitor 命令监测 Redis 上命令执行的情况
- 由于 Redis 自身单线程的原因，切忌慢查询，会阻塞住整个 Redis，可以通过 slowlog get 来查看慢查询日志
-  设置 Redis 最大内存，以防内存用爆
-  使用 redis-rdb-tools 对 rdb 文件进行分析，如每条 key 对应的 value 所占的大小，从 而做量化分析

- 可以使用 Redis Sampler，以统计 Redis 中的数据分布情况


#### 缓存设计的典型方案
在使用缓存系统的时候，还需要考虑缓存设计的问题，重点在于缓存失效时的处理和如何更新缓存。

缓存失效是在使用缓存时不得不面对的问题。 在业务开发中，缓存失效时由于找不到数据，一般会出于容错考虑，从存储层再进行查询，如果有则放入缓存。如果查找的数据在存储层根本就不存在，缓存失去意义，还会给后端服务带来巨大的请求压力，会进一步引起雪崩效应。 这种现象又被称为缓存穿透。

目前常用的解决缓存穿透问题的方案如下:

1. 在底层存储系统之上加一层布隆过滤器，将所有可能存在的数据哈希到一个足够大的 BitMap中， 一个一定不存在的数据会被这个 BitMap 拦截掉

2. 如果数据在存储层查询也为空，那么对此空结果也进行缓存，但要设置合适的失效时间。

更进一步地，解决缓存穿透问题其实和缓存的更新机制是相关的。 缓存更新的常用3种模式如下:

- Cache Aside Pattern：应用程序以数据库为准，失效则从底层存储更新，更新数据先写人数据库再更新缓存。 这是最常用的缓存更新模式。

- Read/Write Through Pattern：以缓存为准，应用只读/写缓存，但是需要保证数据同步更新到数据库中。

- Write Behind Caching Pattern：以缓存为准，应用只读/写缓存，数据异步更新到数据库，不保证数据正确写回，会丢数据。 可以采用 Write Ahead Logging等机制避免。

如上，在缓存失效时采用何种策略去更新缓存，直接决定了能否解决缓存穿透的问题。 Cache Aside Pattern 中缓存失效从底层存储更新，无法避免缓存穿透的问题。 基于以上3种模式，采用下面更为细化的更新机制可以在一定程度上避免缓存穿透的问题：

- 缓存失效时，用加锁或者队列的方式单线程/进程去更新缓存并等待结果
- 缓存失效时，先使用旧值，同时异步（控制为同时只有一个线程/进程）更新缓存，缓存更新失败则抛出异常 或 延续旧值的有效期

- 数据写入或者修改时，更新数据存储后再更新缓存。缓存失效时即认为数据不存在
- 数据写入或者修改时，只更新缓存，使用单独线程周期批量刷新缓存到底层存储。缓存失效时即认为数据不存在。 这种方案不能保障数据的安全性，有可能会丢数据

- 采用单独线程/进程周期将数据从底层存储放到缓存中。 缓存失效时即认为数据不存在。这种方案无法保证缓存数据和底层存储的数据强一致性

如果一开始设计缓存结构的时候注意切分粒度，把缓存力度划分得细一点，那么缓存命中率相对会高， 也能在一定程度上避免缓存穿透的问题。 

此外，还可以在后端做流量控制、服务降级或者动态扩展，以应对缓存穿透带来的访问压力。


#### 查看信息
server.maxmemory

#### 发布/订阅 dict

#### 主从复制
- 全同步
- 部分同步：积压空间

#### 事务
multi exec discard watch

---
https://juejin.im/book/5afc2e5f6fb9a07a9b362527/section/5b4c5158f265da0fa50a0b17
