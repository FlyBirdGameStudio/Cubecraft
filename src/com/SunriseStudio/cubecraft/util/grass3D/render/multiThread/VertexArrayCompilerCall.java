package com.SunriseStudio.cubecraft.util.grass3D.render.multiThread;

import com.SunriseStudio.cubecraft.util.collections.ArrayQueue;
import com.SunriseStudio.cubecraft.util.collections.Pair;
import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;

import java.util.concurrent.Callable;

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
