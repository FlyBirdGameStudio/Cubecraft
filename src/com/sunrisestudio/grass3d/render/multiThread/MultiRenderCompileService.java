package com.sunrisestudio.grass3d.render.multiThread;

import com.sunrisestudio.util.container.ArrayQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRenderCompileService<V extends VertexArrayCompileCallable> {
    private final ArrayQueue<DrawCompile> multiDrawResult=new ArrayQueue<>();
    private final ExecutorService multiDrawService;

    public MultiRenderCompileService(int max){
         this.multiDrawService = Executors.newFixedThreadPool(max);
    }

    public void startDrawing(V v){
        this.multiDrawService.submit(new VertexArrayCompilerCall<>(v,multiDrawResult));
    }

    public int getResultSize() {
        return this.multiDrawResult.size();
    }

    public DrawCompile get(){
        return this.multiDrawResult.poll();
    }
}
