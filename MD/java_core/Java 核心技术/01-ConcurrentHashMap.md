### 第10讲 | ConcurrentHashMap如何实现高效地线程安全？

Collections.synchronizedMap 粗粒度

利用并发包提供的线程安全容器类，它提供了：
- 各种并发容器，比如 ConcurrentHashMap、CopyOnWriteArrayList。
- 各种线程安全队列（Queue/Deque），如 ArrayBlockingQueue、SynchronousQueue。
- 各种有序容器的线程安全版本等

分段锁（默认16） + volatile

```
static class Node<K, V> implements Map.Entry<K, V> {
    final int hash
    final K key
    volatile V val
    volatile Node<K, V> next
}
```

- 并发的 put() 如何实现:

首先是通过二次哈希避免哈希冲突，然后以 Unsafe 调用方式，直接获取相应的 Node，然后进行线程安全的 put 操作：

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K, V>[] tab = table;;) {
        Node<K, V> f; 
        int n, i, fh; 
        K fk; 
        V fv;
        if (tab == null || (n = tab.length) == 0) {
            // 初始化操作
            tab = initTable();
        } else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 利用 CAS 去进行无锁线程安全操作，如果 bin 是空的
            if (casTabAt(tab, i, null, new Node<K, V>(hash, key, value)))
                break; 
        } else if ((fh = f.hash) == MOVED) {
            tab = helpTransfer(tab, f);
        } else if (onlyIfAbsent // 不加锁，进行检查
                 && fh == hash
                 && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                 && (fv = f.val) != null)
            return fv;
        } else {
            V oldVal = null;
            synchronized (f) {
                   // 细粒度的同步修改操作... 
                }
            }
            // Bin 超过阈值，进行树化
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}

// 初始化操作实现在 initTable()，这是一个典型的 CAS 使用场景，利用 volatile 的 sizeCtl 作为互斥手段：
// 如果发现竞争性的初始化，就 spin 在那里，等待条件恢复；否则利用 CAS 设置排他标志。如果成功则进行初始化；否则重试
private final Node<K, V>[] initTable() {
    Node<K, V>[] tab; 
    int sc; //sizeCtl
    while ((tab = table) == null || tab.length == 0) {
        // 如果发现冲突，进行 spin 等待
        if ((sc = sizeCtl) < 0)
            Thread.yield(); 
        // CAS 成功返回 true，则进入真正的初始化逻辑
        else if (U.compareAndSetInt(this, SIZECTL, sc, -1)) {
            try {
                if ((tab = table) == null || tab.length == 0) {
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    
                    Node<K, V>[] nt = (Node<K, V>[]) new Node<?, ?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}

// 更多细节通过使用 Unsafe 进行了优化，例如 tabAt 利用 getObjectAcquire，避免间接调用的开销
static final <K,V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
    return (Node<K, V>)U.getObjectAcquire(tab, ((long)i << ASHIFT) + ABASE);
}
```

- size 操作
```java
final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}

// 我们发现，虽然思路仍然和以前类似，都是分而治之的进行计数，然后求和处理，但实现却基于一个奇怪的 CounterCell。 
// 难道它的数值，就更加准确吗？数据一致性是怎么保证的？
static final class CounterCell {
    volatile long value;
    CounterCell(long x) { value = x; }
}
```

其实，对于 CounterCell 的操作，是基于 java.util.concurrent.atomic.LongAdder 进行的，是一种 JVM 利用空间换取更高效率的方法，利用了 Striped64 内部的复杂逻辑。这个东西非常小众，大多数情况下，建议还是使用 AtomicLong，足以满足绝大部分应用的性能需求。

<p>所以，ConcurrentHashMap 的实现是通过重试机制（RETRIES_BEFORE_LOCK，指定重试次数 2），来试图获得可靠值。如果没有监控到发生变化（通过对比 Segment.modCount），就直接返回，否则获取锁进行操作。</p>


#### 使用场景