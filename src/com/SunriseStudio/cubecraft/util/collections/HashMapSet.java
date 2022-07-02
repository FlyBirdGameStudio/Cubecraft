package com.SunriseStudio.cubecraft.util.collections;

import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyComparable;
import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyGetter;

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
}
