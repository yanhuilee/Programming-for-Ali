### 第9讲 | 对比 HashMap、TreeMap有什么不同？

- 理解 Map 相关类似整体结构，尤其是有序数据结构的一些要点
- 从源码去分析 HashMap 的设计和实现要点，理解容量、负载因子等，为什么需要这些参数，如何影响 Map 的性能，实践中如何取舍等
- 理解树化改造的相关原理

#### 1、map 整体结构

大部分使用 Map 的场景，通常就是放入、访问或者删除，而对顺序没有特别要求，HashMap 在这种情况下基本是最好的选择。 

**HashMap 的性能表现非常依赖于哈希码的有效性，请务必掌握 hashCode 和 equals 的一些基本约定**，equals 相等，hashCode 一定要相等。


#### 2、HashMap 源码分析
容量（capacity）和负载因子（load factor）、树化
      
```
Node<K, V>[] table + 链表
```

HashMap 是按照 lazy-load 原则，在首次使用时被初始化（拷贝构造函数除外）
```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

// putVal() 本身逻辑非常集中，从初始化、扩容到树化，全部都和它有关
final V putVal(int hash, K key, V value, boolean onlyIfAbent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int , i;
    
    // 如果表格是 null，resize() 负责初始化
    if ((tab = table) == null || (n = tab.length) = 0)
        // resize()兼顾两个职责，创建初始存储表格，或在容量不足时，进行扩容（resize）
        n = (tab = resize()).length;
    
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        // ...
        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for first 
           treeifyBin(tab, hash);
        //  ... 
     }
     ++modCount;
     if (++size > threshold)
         resize();
     afterNodeInsertion(evict);
}
```

具体键值对在哈希表中的位置（数组 index）取决于下面的位运算：
```java
i = (n - 1) & hash

static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

注意，为什么这里需要将高位数据移位到低位进行异或运算呢？

这是因为有些数据计算出的哈希值差异主要在高位，而 HashMap 里的哈希寻址是忽略容量以上的高位的，那么这种处理就可以有效避免类似情况下的哈希碰撞。

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    
    // 移动到新的数组结构 e 数组结构（扩容的主要开销）
    return newTab;
}
```

###### 为什么我们需要在乎容量和负载因子呢？
因为容量和负载系数决定了可用的桶的数量，空桶太多会浪费空间，如果使用的太满则会严重影响操作的性能。

极端情况下，假设只有一个桶，那么它就退化成了链表，完全不能提供所谓常数时间存取性能。

- 树化改造

```java
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index;
    Node<K,V> e;
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize();
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        // 树化改造逻辑
    }
}
```

那么，为什么 HashMap 要树化呢？

**本质上这是个安全问题。**因为在元素放置过程中，如果一个对象哈希冲突，都被放置到同一个桶里，则会形成一个链表，链表查询是线性的，会严重影响存取的性能。


#### 3、LinkedHashMap

LinkedHashMap 通常提供的是遍历顺序符合插入顺序，它的实现是通过为条目（键值对）维护一个双向链表。

注意，通过特定构造函数，我们可以创建反映访问顺序的实例，所谓的 put、get、compute 等，都算作“访问”。


#### 4、TreeMap

TreeMap 则是基于红黑树的一种提供顺序访问的 Map，和 HashMap 不同，它的 get、put、remove 之类操作都是 O（log(n)）的时间复杂度，具体顺序可以由指定的 Comparator 来决定，或者根据键的自然顺序来判断。

<p>我在上一讲留给你的思考题提到了，构建一个具有优先级的调度系统的问题，其本质就是个典型的优先队列场景，Java 标准库提供了基于二叉堆实现的 PriorityQueue，它们都是依赖于同一种排序机制，当然也包括 TreeMap 的马甲 TreeSet。</p>

<p>类似 hashCode 和 equals 的约定，为了避免模棱两可的情况，自然顺序同样需要符合一个约定，就是 compareTo 的返回值需要和 equals 一致，否则就会出现模棱两可情况。</p>

```java
public V put(K key, V value) {
    Entry<K,V> t = …
    cmp = k.compareTo(t.key);
    if (cmp < 0)
        t = t.left;
    else if (cmp > 0)
        t = t.right;
    else
        return t.setValue(value);
}
```

解决哈希冲突有哪些典型方法呢？
