package io.flybird.util;

public class Counter {
    private int count=0;

    public void add(){
        count+=1;
    }

    public void release(){
        count-=1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
