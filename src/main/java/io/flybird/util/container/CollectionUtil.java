package io.flybird.util.container;

import io.flybird.cubecraft.world.IWorld;

import java.util.*;

public class CollectionUtil {
    public static <E>void iterateList(List<E> list, ListIterationAction<E> action){
        for(E item:list){
            action.action(item);
        }
    }

    public static <K,V>void iterateMap(Map<K,V> map, MapIterationAction<K,V> action){
        Set<K> keys=map.keySet();
        for(K key:keys){
            action.action(key,map.get(key));
        }
    }

    public static <T> HashMap<String, T> wrap(String id, T t) {
        HashMap<String,T> map=new HashMap<>();
        map.put(id, t);
        return map;
    }

    public interface ListIterationAction<E>{
        void action(E item);
    }

    public interface MapIterationAction<K,V>{
        void action(K key,V item);
    }


    public static <T>List<T> pack(T... item){
        return Arrays.asList(item);
    }
}
