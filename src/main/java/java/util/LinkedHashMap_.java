package java.util;

/**
 * @Author: Lee
 * @Date: 2019/04/15 16:43
 */
public class LinkedHashMap_<K, V> extends HashMap_<K, V> {

    static class Entry<K, V> extends HashMap_.Node<K, V> {
        Entry<K, V> before, after;

        public Entry(int hash, K key, V value, HashMap_.Node<K, V> next) {
            super(hash, key, value, next);
        }
    }
}
