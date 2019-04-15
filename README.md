## Programming-for-Ali
> `Programming-for-Ali`：新的编程思想，我要变得更强！

#### 基础扎实，综合素质过硬

- 数据库模型设计，架构，性能优化，常用配置
- ai: 算法
- backend: 后台
- frontend: 前端
- system: 内存模型，网络通信，多线程


| 📊 | ⚔️ | 🖥 | 🚏 | 🏖  | 🌁 | 📮 | 🔍 | 🚀 |
| :--------: | :---------: | :---------: | :---------: | :---------: | :---------:| :---------: | :-------: | :-------:|
| [基本功](#基本功) | [开源框架](#开源框架) | [数据结构](#数据结构与算法) | [大牛](#大牛) | [前端框架] | 大数据 | [分布式](#分布式相关) | 狼性 | 机器学习 |

---
### 基本功
> IO/NIO/socket/multi thread/collection/concurrency

书单
> Java工程师技术修炼之道
> 实战Java高并发程度设计
> 剑指Offer
> 算法导论
> 《Java 并发编程实战》
> 深入分布式缓存 从原理到实践
> 逆流而上，阿里巴巴技术成长之路
> 学C编程，也可以卡通一点
> 代码重构2


#### java 方面
1. 核心语法：try-with-resource, switch string, Lambda, Stream
2. 集合类：HashMap，ArrayList, LinkedList, HashSet, TreeSet
	- 包括线程安全集合: ConcurrentHashMap，ConcurrentLinkedQueue
	- 了解实现原理，查询修改的性能和使用场景
3. 工具类：Guava Commons FastJson
4. 高级特性
  - 并发编程: Executors提供的并发工具，Fork/Join框架，CountDownLatch/Semaphore/CyclicBarrier等同步工具

反射机制：提供第三方开发者扩展能力（Servlet容器，JDBC连接）
动态代理: AOP-日志，安全，事务；RPC

算法：排序，索引，存储，去重，平滑等

> [Java核心技术](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/java_core/Java%20核心技术/《Java%20核心技术》.md)


- [知识点](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/01-basic_training.md)
- [集合类](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/02-collection.md)

- [多线程](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/03-multithread.md)
- [JVM](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/04-jvm.md)
- [设计模式]()

---
### 开源框架
#### Spring 生态
- [ ] [Spring注解式开发](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/opensource/01-Spring注解式开发.md)
- [ ] 整合 MyBatis
- [ ] Spring 框架中的设计模式

```
ioc/aop/声明式事务/扩展原理/servlet3
refresh()
监听器过滤器
servlet -- tomcat
02-IoC容器初始化过程
```

#### MyBatis
- 整体架构
- 核心部件
- SQL 执行
- 缓存机制

#### MySQL
> 体系结构，备份恢复，主从复制，高可用集群架构，优化，故障排查，新版本特性，监控，升级等相关知识点

- [存储引擎：InnoDB]()
- 连接池 Druid
SQL 优化经验：分库分表，查看执行计划
主从配置
explain

事务执行过程中宕机的应对处理方式？http://www.mybatis.cn/archives/75.html
	事务开启时，事务中的操作，都会先写入存储引擎的日志缓冲中，在事务提交之前，这些缓冲的日志都需要提前刷新到磁盘上持久化
	日志分为两种类型：redo log和undo log
数据库插入重复如何处理？分布式锁
数据库链接中断如何处理？http://www.mybatis.cn/archives/71.html

---
### nginx
	配置
	静态代理
	负载均衡
	动静分离
	虚拟主机

---
### 分布式相关
- [消息队列](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/13-distributed_server.md)


---
### 联系作者
> liyanlang#gmail.com
