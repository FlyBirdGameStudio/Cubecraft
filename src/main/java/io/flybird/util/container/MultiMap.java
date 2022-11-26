package io.flybird.util.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MultiMap<K,V> implements Map<K,V> {
    private HashMap<K,V> kvHashMap=new HashMap<>();
    private HashMap<V,K> vkHashMap=new HashMap<>();

    public V put(K k, V v){
        this.kvHashMap.put(k,v);
        this.vkHashMap.put(v,k);
        return v;
    }

    @Override
    public int size() {
        return this.kvHashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.kvHashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.kvHashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.kvHashMap.containsKey(value);
    }

    public V get(Object obj){
        return this.kvHashMap.get(obj);
    }

    public K of(V v){
        return this.vkHashMap.get(v);
    }

    public V remove(Object k){
        this.vkHashMap.remove(this.kvHashMap.get(k));
        return this.kvHashMap.remove(k);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        CollectionUtil.iterateMap(m, this::put);
    }

    @Override
    public void clear() {
        this.kvHashMap.clear();
        this.vkHashMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.kvHashMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.kvHashMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.kvHashMap.entrySet();
    }
}
