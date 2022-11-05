package io.flybird.util.container.keyMap;

import java.util.ArrayList;
import java.util.Iterator;

public class KeyMap <K extends KeyComparable<K>,E extends KeyGetter<K>>{
    public ArrayList<E> elements =new ArrayList<>();

    public void add(E e){
        if(!containsKey(e.getKey())){
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
        for (E e:this.elements){
            if(e.getKey().compare(key)){
                return true;
            }
        }
        return false;
    }

    public E get(K key) {
        Iterator<E> it=this.elements.iterator();
        while (it.hasNext()){
            E e=it.next();
            if(e.getKey().compare(key)){
                return e;
            }
        }
        return null;
    }

    public E remove(K key) {
        Iterator<E> it=this.elements.iterator();
        while (it.hasNext()){
            E e=it.next();
            if(e.getKey().compare(key)){
                it.remove();
                return e;
            }
        }
        return null;
    }
}
