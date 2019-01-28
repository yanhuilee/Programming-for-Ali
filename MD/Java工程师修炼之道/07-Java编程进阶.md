1. [内存管理](#Java内存管理)
2. [网络编程](#Java网络编程)
3. [并发编程](#Java并发编程)

### Java 内存管理
> 了解如何做内存管理才能从根本上掌握 Java 的编程技巧，避免一些内存问题的出现

#### 1. JVM 虚拟机内存

运行时内存区、内存分配过程、对象访问、内存溢出、垃圾回收理论、吞吐量与响应时间、GC流程和算法、分代回收、CMS/G1

2. 垃圾回收理论

3. 常用垃圾回收器

---

### Java 网络编程

- IO：内存，网络，磁盘
- 阻塞和非阻塞

1. 常见网络 I/O 模型
- 同步阻塞，同步非阻塞
- I/O复用：多路IO就绪通知，select、poll、epoll
- 信号驱动
- 异步非阻塞

2. Java 网络编程模型
BIO/NIO/AIO

---
### Java 并发编程
> 并发是提升应用性能非常关键的手段

1. 并发原理: 并发与并行，Java内存模型，重排序，并发问题
2. 并发思路：happens-before，原子性、可见性（final/volatile/synchronized）、有序性，

3. 并发工具: [锁](#锁)，[无锁](#无锁)，并发集合，同步器，并发框架（Executor、Fork/Join/Actor）

#### 锁
ReentrantLock 和 synchronized 相比更加灵活，且具有等待可中断、可实现公平锁、可以绑定多个条件等特性

- **等待可中断**：当持有锁的线程长期不释放锁时，正在等待的线程可以选择放弃等待
- **公平锁**：多个线程在等待同一个锁时，必须按照申请锁的顺序来依次获得锁；在锁被释放时，任何一个等待锁的线程都有机会获得锁。synchronized 中的锁是非公平的，ReentrantLock 默认也是非公平的
- **绑定多个条件**：synchronized 中，锁对象的 wait、notify 可以实现一个隐含的条件，如果要和多于一个条件相关联，那么不得不额外地添加一个锁。而 ReentrantLock 可以通过 newCondition 方法绑定多个条件

ReentrantLock 通过使用 condition 的 await 和 signal 达到线程间通信的目的

#### 无锁
- **CAS**：即 Compare And Swap，类似于乐观锁的机制。每次更新值的时候都使用旧值与当前值做比较，如果相同则进行更新，否则重试直到成功
- **ThreadLocal**：每个线程都有一份数据的副本，也变不会存在并发问题了
- **不可变对象**：不可变对象自然不会有并发问题

AQS （Abstract Queued Synchronizer ，基于 CAS 来保证线程安全），提供了一些模板方法供子类实现，从而实现了不同的同步器（如 Sync、FairSync、NonfairSync 等） ReentrantLock、ReentrantReadWriteLock 以及
ThreadPoolExecutor 都使用了 AQS。

4. 并发编程建议

---
### Java 开发利器
1. Apache 工具库－Apache Commons
2. Google 工具库－Guava
3. 最好用的时间库－Joda Time
4. 高效 JSON 处理库－FastJson
5. 高效 Bean 映射框架一一Orika

---
### Java 新特性
