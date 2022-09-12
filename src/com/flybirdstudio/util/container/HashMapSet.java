package com.flybirdstudio.util.container;

import com.flybirdstudio.util.container.keyMap.KeyComparable;
import com.flybirdstudio.util.container.keyMap.KeyGetter;

import java.util.HashMap;

public class HashMapSet<K extends KeyComparable<K>,V extends KeyGetter<K>> {
    public HashMap<Integer,V> map=new HashMap<>();

    public void add(V v){
        if (!map.containsKey(v.getKey().hashCode())){
            map.put(v.getKey().hashCode(),v);
        }
    }

    public V get(K k){
        return this.map.getOrDefault(k.hashCode(),null);
    }

    public boolean contains(K k) {
        return map.containsKey(k.hashCode());
    }

    public int size() {
        return this.map.size();
    }

    public void forceAdd(V v){
        map.put(v.getKey().hashCode(),v);
    }
}