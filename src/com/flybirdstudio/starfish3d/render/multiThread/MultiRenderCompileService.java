package com.flybirdstudio.starfish3d.render.multiThread;

import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.task.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRenderCompileService<V extends VertexArrayCompileCallable> {
    private final ArrayQueue<DrawCompile> multiDrawResult=new ArrayQueue<>();
    private final ExecutorService pool;

    public MultiRenderCompileService(String name,int max){
         this.pool=Executors.newFixedThreadPool(max);
    }

    public void startDrawing(V v){
        this.pool.submit(new VertexArrayCompilerCall<>(v,this.multiDrawResult));
    }

    public int getResultSize() {
        return this.multiDrawResult.size();
    }

    public DrawCompile get(){
        return this.multiDrawResult.poll();
    }
}
