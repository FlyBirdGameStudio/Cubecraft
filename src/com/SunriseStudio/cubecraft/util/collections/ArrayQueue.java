package com.SunriseStudio.cubecraft.util.collections;

import java.util.*;
import java.util.function.Predicate;

public class ArrayQueue <E extends Comparable<E>> {
    public PriorityQueue<PriorityAdapter<E>> items=new PriorityQueue<>();

    //information
    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean contains(Object o) {
        return this.items.contains(o);
    }

    //operation
    //public Iterator<E> iterator() {
        //return this.items.iterator();
    //}

    public <T> T[] toArray(T[] a) {
        return this.items.toArray(a);
    }

    public void add(E e) {
        items.offer(new PriorityAdapter<>(e));
    }

    public void addAll(List<E> all){
        for (E e:all){
            this.add(e);
        }
    }

    public E poll(){
        return this.items.poll().e;
    }
    public List<E> pollAll(int count){
        ArrayList<E> returns=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            returns.add(this.poll());
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
