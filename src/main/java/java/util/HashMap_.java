package java.util;

/**
 * @Author: Lee
 * @Date: 2019/04/14 00:46
 */
public class HashMap_<K, V> {

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }

    static final class TreeNode<K, V> extends LinkedHashMap_.Entry<K, V> {
        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;
        boolean red;

        TreeNode(int hash, K key, V value, HashMap_.Node<K, V> next) {
            super(hash, key, value, next);
        }

        final void split(HashMap_<K, V> map, Node<K, V>[] tab, int index, int bit) {

        }

    }

    /* ---------------- Fields -------------- */
    transient Node<K, V>[] table;
    // The next size value at which to resize (capacity * load factor).
    int threshold;

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        // 记住扩容前的数组长度和最大容量
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            // 超过数组在java中最大容量就无能为力了，冲突就只能冲突
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } //长度和最大容量都扩容为原来的二倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        // 更新新的最大容量为扩容计算后的最大容量
        threshold = newThr;
        // 更新扩容后的新数组长度
        Node<K,V>[] newTab = (Node<K, V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            // 遍历老数组下标索引
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                // 如果老数组对应索引上有元素则取出链表头元素放在e中
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    // 如果老数组j下标处只有一个元素则直接计算新数组中位置放置
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else if (e instanceof TreeNode)
                        // 如果是树结构进行单独处理
                        ((TreeNode<K, V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        //能进来说明数组索引j位置上存在哈希冲突的链表结构
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        //循环处理数组索引j位置上哈希冲突的链表中每个元素
                        do {
                            next = e.next;
                            //判断key的hash值与老数组长度与操作后结果决定元素是放在原索引处还是新索引
                            if ((e.hash & oldCap) == 0) {
                                //放在原索引处的建立新链表
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                //放在新索引（原索引+oldCap）处的建立新链表
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            //放入原索引处
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            //放入新索引处
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
}
