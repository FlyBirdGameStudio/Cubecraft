package io.flybird.starfish3d.render.multiThread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.flybird.util.container.ArrayQueue;

import java.util.concurrent.ThreadFactory;

public class MultiRenderCompileService<V extends VertexArrayCompileCallable> implements IDrawService<V> {
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("ChunkCompilerCall-%d")
            .setDaemon(true)
            .build();

    private final ArrayQueue<DrawCompile> multiDrawResult = new ArrayQueue<>();
    private final ArrayQueue<IDrawCompile> all = new ArrayQueue<>();
    private final ArrayQueue<V> cache = new ArrayQueue<>();

    private final Thread[] threads;
    private final CompilerWorker<?>[] workers;

    public MultiRenderCompileService(int max) {
        this.threads=new Thread[max];
        this.workers=new CompilerWorker[max];
        for (int i=0;i<max;i++){
            CompilerWorker<V> worker=new CompilerWorker<>(this.multiDrawResult, this.all,this.cache);
            this.workers[i]=worker;
            this.threads[i]=THREAD_FACTORY.newThread(worker);
            this.threads[i].start();
        }
    }

    //todo:Caused by java.lang.IndexOutOfBoundsException: Index: 0, Size: -1
    @Override
    public void startDrawing(V v) {
        this.cache.add(v);
    }

    @Override
    public void startDirect(V v){
        if(this.cache.contains(v)) {
            this.cache.remove(v);
            new Thread(new VertexArrayCompilerCall<>(v, this.multiDrawResult, this.all)).start();
        }else{
            this.startDrawing(v);
        }
    }

    @Override
    public int getResultSize() {
        return this.multiDrawResult.size();
    }

    @Override
    public DrawCompile getAvailableCompile() {
        return this.multiDrawResult.poll();
    }

    @Override
    public IDrawCompile getAllCompile() {
        return this.all.poll();
    }

    @Override
    public int getAllResultSize() {
        return this.all.size();
    }

    @Override
    public ArrayQueue<V> getCache() {
        return this.cache;
    }
}
