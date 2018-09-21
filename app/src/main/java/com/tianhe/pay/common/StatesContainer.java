package com.tianhe.pay.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public interface StatesContainer<K, V> {
    V get(K key);

    void clear();

    void save(K key, V state);

    boolean contains(K key);

    V remove(K key);

    public static class HashStatesContainer<K, V> implements StatesContainer<K, V> {
        private HashMap<K, V> states = new HashMap<>();
        @Override
        public V get(K key) {
            return states.get(key);
        }

        @Override
        public void clear() {
            states.clear();
        }

        @Override
        public void save(K key, V state) {
            states.put(key, state);
        }

        @Override
        public boolean contains(K key) {
            return states.containsKey(key);
        }

        @Override
        public V remove(K key) {
            return states.remove(key);
        }
    }

    public class ListStatesContainer<K, V> implements StatesContainer<K, V> {
        private StatesContainer<K,V> wrap;
        private List<K> keyList;

        public ListStatesContainer(StatesContainer<K, V> wrap) {
            this.wrap = wrap;
            keyList = new LinkedList<>();
        }

        public List<K> getKeys() {
            return Collections.unmodifiableList(keyList);
        }

        public void setKeys(Collection<? extends K> keys) {
            keyList.addAll(keys);
        }

        @Override
        public V get(K key) {
            return wrap.get(key);
        }

        @Override
        public void clear() {
            keyList.clear();
            wrap.clear();
        }

        @Override
        public void save(K key, V state) {
            keyList.add(key);
            wrap.save(key, state);
        }

        @Override
        public boolean contains(K key) {
            return keyList.contains(key) && wrap.contains(key);
        }

        @Override
        public V remove(K key) {
            keyList.remove(key);
            return wrap.remove(key);
        }

        /**
         * 只移除当前key List所对应的value. 作用不等效于{@link ListStatesContainer#clear }
         * （因为包装的SavedStates可能还有其他state被保存, 但与当前对象无关）
         */
        public void clearByKeys() {
            Iterator<K> keyIter = keyList.iterator();
            while (keyIter.hasNext()) {
                K key = keyIter.next();
                wrap.remove(key);
            }
            keyList.clear();
        }
    }
}
