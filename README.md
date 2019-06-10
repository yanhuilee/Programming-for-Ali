## Programming-for-Ali
> `Programming-for-Ali`：新的编程思想，我要变得更强！

#### 基础扎实，综合素质过硬

- 数据库模型设计，架构，性能优化，常用配置
- ai: 算法
- architect: 架构
- backend: 后台
- frontend: 前端
- system: 内存模型，网络通信，多线程


| 📊 | ⚔️ | 🖥 | 🚏 | 🏖  | 🌁 | 📮 | 🔍 | 🚀 |
| :--------: | :---------: | :---------: | :---------: | :---------: | :---------:| :---------: | :-------: | :-------:|
| [基本功](#基本功) | [开源框架](#开源框架) | [数据结构](#数据结构与算法) | [前端框架] | 大数据 | [分布式](#分布式相关) | 微服务 | 狼性 |

---
### 基本功
1. 核心语法：try-with-resource, switch string, Lambda, Stream
2. 集合类：HashMap，ArrayList, LinkedList, HashSet, TreeSet
	- 包括线程安全集合: ConcurrentHashMap，ConcurrentLinkedQueue
	- 了解实现原理，查询修改的性能和使用场景
3. 工具类：Guava，Apache Commons，FastJson
4. 高级特性
  - 并发编程: Executors提供的并发工具，Fork/Join框架，CountDownLatch/Semaphore/CyclicBarrier等同步工具
  - 网络编程：BIO/NIO/AIO
  - 序列化
  - RPC：Thrift（Http/TCP）/Hessian（Http）/Dubbo（TCP）/RMI

反射机制：提供第三方开发者扩展能力（Servlet容器，JDBC连接）
动态代理: AOP-日志，安全，事务；RPC

> [Java核心技术](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/java_core/Java%20核心技术/《Java%20核心技术》.md)

#### JVM相关
- 类加载机制：双亲委派原则，当前类加载器需要先去请求父加载器加载当前类，无法完成才自己去尝试加载

OSGI框架则打破了此机制，采用了平等的、网状的类加载机制，以实现模块化的加载方案

- 运行时内存：程序计数器，堆栈，方法区，堆外内存

- Java内存模型：主内存 + 线程私有内存的模型是线程安全问题产生的根本

- GC原理和调优：对GC各种参数做优化配置，快速定位问题和解决

- 性能调优和监控工具：jmap/jstack/jcmd/JConsole/jinfo等

BTrace 是一款强大的在线问题动态排查工具，无须重启即可动态插入一些代码逻辑，从而拦截代码执行逻辑打印日志并排查问题


##### 书单
> 逆流而上，阿里巴巴技术成长之路
> 学C编程，也可以卡通一点
> 代码重构2
> Spring Boot+Vue全栈开发实战
> Spring源码深度解析（第2版）
> 深入分布式缓存 从原理到实践（2，13，14）
> 亿级流量网站架构核心技术
> Netty实战 电子版

掘金：Redis


---
### 开源框架
#### Spring 生态
- [ ] [Spring注解式开发](https://github.com/yanhuilee/Programming-for-Ali/blob/master/MD/opensource/01-Spring注解式开发.md)
- [ ] 整合 MyBatis

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
> liyanlang@gmail.com
