package li.util.concurrent;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Lee
 * @Date: 20/8/30 15:52
 */
public class LiConcurrentHashMap<K, V> extends AbstractMap<K, V> {

    /* ---------------- Table element access -------------- */
    static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
        return U.getObjectVolatile(tab, ((long) i << ))
    }

    /* ---------------- Fields -------------- */
    /**
     * 大小始终是2的幂。由迭代器直接访问。
     */
    transient volatile Node<K, V>[] table;

    /**
     * 表初始化和大小调整控制。如果为负，则表将被初始化或调整大小：-1用于初始化，
     * 否则-（1 +活动的调整大小线程数）。否则，当table为null时，保留创建时要使用的初始表大小，或者默认为0。
     * 初始化后，保留下一个要调整表大小的元素计数值。
     */
    private transient volatile int sizeCtl;

    // 普通节点哈希的可用位
    static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash

    /* ---------------- Constants -------------- */

    /**
     * The largest possible table capacity.  This value must be
     * exactly 1<<30 to stay within Java array allocation and indexing
     * bounds for power of two table sizes, and is further required
     * because the top two bits of 32bit hash fields are used for
     * control purposes.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The default initial table capacity.  Must be a power of 2
     * (i.e., at least 1) and at most MAXIMUM_CAPACITY.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * 将散列的较高位散布（XOR）降低，并且也将最高位强制为0。
     * 由于该表使用2的幂次掩码，因此仅在当前掩码上方的位中变化的哈希集将始终发生冲突。
     * （众所周知的示例是在小表中保存连续整数的Float键集。）因此，我们应用了一种变换，将向下传播较高位的影响。
     * 在速度，实用性和位扩展质量之间需要权衡。由于许多常见的哈希集已经合理分布（因此无法从扩展中受益），
     * 并且由于我们使用树来处理容器中的大量冲突集，因此我们仅以最便宜的方式对一些移位后的位进行XOR，以减少系统损失，以及合并最高位的影响，
     * 否则由于表范围的限制，这些位将永远不会在索引计算中使用。
     */
    static final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }

    /**
     * 对于给定的所需容量，返回两个表大小的幂
     * See Hackers Delight, sec 3.2
     */
    private static final int tableSizeFor(int c) {
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /* ---------------- Public operations -------------- */

    /**
     * Creates a new, empty map with the default initial table size (16).
     */
    public LiConcurrentHashMap() {
    }

    /**
     * Creates a new, empty map with an initial table size
     * accommodating the specified number of elements without the need
     * to dynamically resize.
     *
     * @param initialCapacity The implementation performs internal
     *                        sizing to accommodate this many elements.
     * @throws IllegalArgumentException if the initial capacity of
     *                                  elements is negative
     */
    public LiConcurrentHashMap(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException();
        int cap = (initialCapacity >= MAXIMUM_CAPACITY >>> 1) ?
                MAXIMUM_CAPACITY :
                tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1);
        this.sizeCtl = cap;
    }

    /**
     * 将指定的键映射到此表中的指定值
     * 键或值都不能为null
     *
     * <p>The value can be retrieved by calling the {@code get} method
     * with a key that is equal to the original key.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}
     * @throws NullPointerException if the specified key or value is null
     */
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    private V putVal(K key, V value, boolean onlyIfAbsent) {
        // 空判
        if (key == null || value == null) throw new NullPointerException();
        // (h ^ (h >>> 16)) & 0x7fffffff
        int hash = spread(key.hashCode());
        int binCount = 0;

        for (Node<K, V>[] tab = table; ; ) {
            Node<K, V> f;
            int n, i, fh;
            if (tab == null || (n == tab.length) == 0) {
                // 初始化
                tab = initTable();
            } else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {

            }
        }
        return null;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K, V> next;

        public Node(int hash, K key, V val, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return val;
        }

        @Override
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public final boolean equals(Object o) {
            Object k, v, u;
            Map.Entry<?, ?> e;
            return ((o instanceof Map.Entry) &&
                    (k = (e = (Map.Entry<?, ?>) o).getKey()) != null &&
                    (v = e.getValue()) != null &&
                    (k == key || k.equals(key)) &&
                    (v == (u = val) || v.equals(u)));
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ val.hashCode();
        }

        /**
         * Virtualized support for map.get(); overridden in subclasses.
         */
        Node<K, V> find(int h, Object k) {
            Node<K, V> e = this;
            if (k != null) {
                do {
                    K ek;
                    if (e.hash == h &&
                            ((ek = e.key) == k || (ek != null && k.equals(ek)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
            return null;
        }
    }

    /**
     * 使用sizeCtl中记录的大小初始化表
     */
    private final Node<K, V>[] initTable() {
        Node<K, V>[] tab;
        int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl) < 0) {
                Thread.yield();  // 初始化竞赛失败；旋转一下
            } else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
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

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;

    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            Class<?> k = LiConcurrentHashMap.class;
            SIZECTL = U.objectFieldOffset(k.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = U.objectFieldOffset
                    (k.getDeclaredField("transferIndex"));
            BASECOUNT = U.objectFieldOffset
                    (k.getDeclaredField("baseCount"));
            CELLSBUSY = U.objectFieldOffset
                    (k.getDeclaredField("cellsBusy"));
            Class<?> ck = CounterCell.class;
            CELLVALUE = U.objectFieldOffset
                    (ck.getDeclaredField("value"));
            Class<?> ak = Node[].class;
            ABASE = U.arrayBaseOffset(ak);
            int scale = U.arrayIndexScale(ak);
            if ((scale & (scale - 1)) != 0)
                throw new Error("data type scale not a power of two");
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
