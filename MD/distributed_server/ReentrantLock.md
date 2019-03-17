ReentrantLock的基本实现可以概括为：
1. 先通过CAS尝试获取锁。如果此时已经有线程占据了锁，那就加入CLH队列并且被挂起。

2. 当锁被释放之后，排在CLH队列队首的线程会被唤醒，然后CAS再次尝试获取锁。在这个时候，如果：

- 非公平锁：如果同时还有另一个线程进来尝试获取，那么有可能会让这个线程抢先获取；
- 公平锁：如果同时还有另一个线程进来尝试获取，当它发现自己不是在队首的话，就会排到队尾，由队首的线程获取到锁。

来分析一下：
```java
// 尝试获取锁
final boolean nonfairTryAcquire(int acquires) {
   final Thread current = Thread.currentThread();
   int c = getState();
   // 无人占用
   if (c == 0) {
       if (compareAndSetState(0, acquires)) {
           setExclusiveOwnerThread(current);
           return true;
       }

   // if 当前线程持有锁（可重入）
   } else if (current == getExclusiveOwnerThread()) {
       int nextc = c + acquires;
       if (nextc < 0) // overflow
           throw new Error("Maximum lock count exceeded");
       setState(nextc);
       return true;
   }
   return false;
}
```

```java
// 获取锁失败加入CLH队尾
final boolean acquireQueued(final Node node, int arg) {
   boolean failed = true;
   try {
       boolean interrupted = false;
       for (;;) {
           final Node p = node.predecessor();
           // 前序节点是head，则CAS再尝试获取一次
           if (p == head && tryAcquire(arg)) {
               setHead(node);
               p.next = null; // help GC
               failed = false;
               return interrupted;
           }
           // 根据前序节点的状态判断是否需要阻塞
           if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
               interrupted = true;
       }
   } finally {
       if (failed)
           cancelAcquire(node);
   }
}
private final boolean parkAndCheckInterrupt() {
   LockSupport.park(this);
   return Thread.interrupted();
}
```