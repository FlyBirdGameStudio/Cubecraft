package com.sunrisestudio.grass3d.render.multiThread;

import com.sunrisestudio.util.container.ArrayQueue;

public class VertexArrayCompilerCall<T extends VertexArrayCompileCallable> implements Runnable {
    public T compileCallable;
    public ArrayQueue<DrawCompile> queue;

    public VertexArrayCompilerCall(T compileCallable,ArrayQueue<DrawCompile> queue){
        this.compileCallable=compileCallable;
        this.queue=queue;
    }

    @Override
    public void run(){
        this.queue.add(new DrawCompile(this.compileCallable.getList(),this.compileCallable.compile()));
    }
}
