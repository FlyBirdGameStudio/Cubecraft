package com.flybirdstudio.starfish3d.render.multiThread;

import com.flybirdstudio.util.container.ArrayQueue;

public class VertexArrayCompilerCall<T extends VertexArrayCompileCallable> implements Runnable {
    public T compileCallable;
    public ArrayQueue<DrawCompile> queue;

    public VertexArrayCompilerCall(T compileCallable,ArrayQueue<DrawCompile> queue){
        this.compileCallable=compileCallable;
        this.queue=queue;
    }

    @Override
    public void run(){
        DrawCompile[] res=this.compileCallable.compile();
        for (DrawCompile dc:res) {
            if(dc!=null) {
                this.queue.add(dc);
            }
        }
    }
}
