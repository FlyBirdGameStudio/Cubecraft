package io.flybird.starfish3d.render.multiThread;

import io.flybird.util.container.ArrayQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiRenderCompileService<V extends VertexArrayCompileCallable> implements IDrawService<V>{
    private final ArrayQueue<DrawCompile> multiDrawResult=new ArrayQueue<>();
    private final ArrayQueue<IDrawCompile> all =new ArrayQueue<>();
    private final ExecutorService pool;

    public MultiRenderCompileService(String name,int max){
         this.pool=Executors.newFixedThreadPool(max);
    }

    @Override
    public void startDrawing(V v){
        this.pool.submit(new VertexArrayCompilerCall<>(v,this.multiDrawResult,this.all));
    }

    @Override
    public int getResultSize() {
        return this.multiDrawResult.size();
    }

    @Override
    public DrawCompile getAvailableCompile(){
        return this.multiDrawResult.poll();
    }

    @Override
    public IDrawCompile getAllCompile(){
        return this.all.poll();
    }

    @Override
    public int getAllResultSize() {
        return this.all.size();
    }
}
