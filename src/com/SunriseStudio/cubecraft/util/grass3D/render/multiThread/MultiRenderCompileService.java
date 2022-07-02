package com.SunriseStudio.cubecraft.util.grass3D.render.multiThread;

import com.SunriseStudio.cubecraft.util.collections.ArrayQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
