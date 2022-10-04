package com.flybirdstudio.util.container;

import java.util.*;

public class ArrayQueue <E> {
    public PriorityQueue<PriorityAdapter<E>> items=new PriorityQueue<>();
    public HashSet<Integer> e=new HashSet<>();

    //information
    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean contains(E e) {
        return this.e.contains(e.hashCode());
    }

    public <T> T[] toArray(T[] a) {
        return this.items.toArray(a);
    }

    public void add(E e) {
        if(!contains(e)) {
            items.offer(new PriorityAdapter<>(e));
            this.e.add(e.hashCode());
        }
    }

    public void addAll(List<E> all){
        for (E e:all){
            this.add(e);
        }
    }

    public E poll(){
        if(items.size()>0) {
            PriorityAdapter<E> item=this.items.poll();
            return item==null?null:item.e;
        }else{
            return null;
        }
    }
    public List<E> pollAll(int count){
        ArrayList<E> returns=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            E e=this.poll();
            if(e!=null){
                returns.add(e);
            }
        }
        return returns;
    }
    public void clear() {
        this.items.clear();
    }

    public List<E> pollIf(int count , Prediction<E> pollIf){
        ArrayList<E> returns=new ArrayList<>();
        for (PriorityAdapter<E> e:this.items) {
            if(pollIf.If(e.e)){
                returns.add(e.e);
            }//给我standby到满足条件
            if(returns.size()>=count){
                break;
            }
        }
        ;
        return returns;
    }

    public void removeIf(Prediction<E> tester) {
        this.items.removeIf(ePriorityAdapter -> tester.If(ePriorityAdapter.e));
    }

    public void remove(E e) {
        this.items.remove(e);
    }

    public interface Prediction<E>{
        boolean If(E e);
    }

    /**
     * yep,just an adapter to fit for class that not support {@link Comparable<T>}
     * @param e item
     * @param <T> class
     */
    private record PriorityAdapter<T>(T e) implements Comparable<PriorityAdapter>{
        @Override
        public int compareTo(PriorityAdapter o) {
            return 0;
        }
    }
}
