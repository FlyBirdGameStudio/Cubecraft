package com.SunriseStudio.cubecraft.util.collections.uuidMap;

import java.util.*;

public class UUIDMap<E extends UUIDGetter<K>,K extends UUID<K>>{

    public ArrayList<E> elements =new ArrayList<>();

    public void add(E e){
        if(!containsKey(e.getUID())){
            this.elements.add(e);
        }
    }

    public int size() {
        return this.elements.size();
    }

    public boolean isEmpty() {
        return this.size()==0;
    }

    public boolean containsKey(K key) {
        boolean result=false;
        for (E e:this.elements){
            if(e.getUID().compare(key)){
                result=true;
            }
        }
        return result;
    }

    public E get(K key) {
        E result=null;
        Iterator<E> it=this.elements.iterator();
        while (it.hasNext()){
            E e=it.next();
            if(e.getUID().compare(key)){
                result=e;
            }
        }
        return result;
    }

    public E remove(K key) {
        E result=null;
        Iterator<E> it=this.elements.iterator();
        while (it.hasNext()){
            E e=it.next();
            if(e.getUID().compare(key)){
                result=e;
                it.remove();
            }
        }
        return result;
    }
}
