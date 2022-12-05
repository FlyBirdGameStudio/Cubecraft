package io.flybird.util.container;

import java.util.*;

public class ArrayQueue <E> {


    //information
    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(E e) {
        return this.list.contains(e);
    }

    public E[] toArray(E[] a) {
        return this.list.toArray(a);
    }


    public ArrayList<E> list=new ArrayList<>();

    public void add(E e) {
        if(this.list.isEmpty()){
            this.list=new ArrayList<>();
        }
        if(e!=null&&!contains(e)) {
           this.list.add(0,e);
        }
    }

    public void addAll(List<E> all){
        for (E e:all){
            this.add(e);
        }
    }

    public E poll(){
        try {
            if (list.size() > 0) {
                return this.list.remove(this.list.size() - 1);
            } else {
                return null;
            }
        }catch (Exception e){
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
        this.list.clear();
    }

    public List<E> pollIf(int count , Prediction<E> pollIf){
        ArrayList<E> returns=new ArrayList<>(1);
        for (E e:this.list) {
            if(pollIf.If(e)){
                returns.add(e);
            }//给我standby到满足条件
            if(returns.size()>=count){
                break;
            }
        }

        return returns;
    }

    public void removeIf(Prediction<E> tester) {
        this.list.removeIf(tester::If);
    }

    public void remove(E e) {
        this.list.remove(e);
    }

    public void sort(Comparator<E> sorter){
        this.list.sort(sorter);
    }

    public interface Prediction<E>{
        boolean If(E e);
    }
}
