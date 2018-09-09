
> 摘自：《实战Java高并发程序设计》
> 维护人员：**Lee**  
> 创建时间：2018-09-08

#### 1、先要了解的概念
- 同步(Synchronous)、异步(Asyn)
- 并发(Concurrency)、并发(Parallelism)
- 临界区
- 阻塞、非阻塞(Non-Blocking)
- 死锁(Deadlock)、饥饿(Starvation)、活锁(Livelock)

#### 还要了解JMM

##### 线程安全三个特性：原子性，可见性，有序性

有序性：Happen-Before规则

##### 线程安全关键字
volatile synchronized

（synchronized、volatile、ReenreantLock、ThreadLocal）

#### 2、线程基础
- 六种状态

```java
public enum State {
  NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED;
}
```

- 等待（wait）和通知（notify）

必须包含在对应的`synchronized`语句中，都需要首先获得目标对象的一个监视器

#### 3、并发包 j.util.concurrent

AbstractQueuedSynchronizer

ReentrantLock
ReadWriteLock


多线程团队协作：同步控制
ArrayBlockingQueue 有界队列
  Condition
  put() {
    notFull = lock.newCondition()
    lock.lockInterruptibly()
    notFull.await()
  }
  insert() {
    notEmpty.signal()
  }

Semaphore
CountDownLatch
CyclicBarrier: 循环栅栏，要求线程在栅栏处等待，计数器可以重复使用
LockSupport

线程复用：线程池
ThreadPoolExecutor: 可扩展的线程池
  beforeExecute() afterExecute() terminated()

Fork/Join 分而治之

不重复造轮子：并发容器


#### 4、锁
#### 3、并行模式与算法
#### 3、Java 8 与并发
#### 3、使用Akka 构建高并发程序

---
### Java 多线程
- [多线程中的常见问题](https://github.com/crossoverJie/JCSprout/blob/master/MD/Thread-common-problem.md)
- [synchronized 关键字原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/Synchronize.md)
- [多线程的三大核心](https://github.com/crossoverJie/JCSprout/blob/master/MD/Threadcore.md)
- [对锁的一些认知](https://github.com/crossoverJie/JCSprout/blob/master/MD/Java-lock.md)
- [ReentrantLock 实现原理 ](https://github.com/crossoverJie/JCSprout/blob/master/MD/ReentrantLock.md)
- [ConcurrentHashMap 的实现原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/ConcurrentHashMap.md)
- [如何优雅的使用和理解线程池](https://github.com/crossoverJie/JCSprout/blob/master/MD/ThreadPoolExecutor.md)
- [深入理解线程通信](https://github.com/crossoverJie/JCSprout/blob/master/MD/concurrent/thread-communication.md)
- [交替打印奇偶数](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/actual/TwoThread.java)
---
实际工作经验
