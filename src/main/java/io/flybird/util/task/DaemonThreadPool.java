package io.flybird.util.task;

import io.flybird.starfish3d.render.multiThread.VertexArrayCompileCallable;
import io.flybird.util.container.ArrayQueue;

public class DaemonThreadPool {
    private final int threadCount;
    private final LoopWorker[] threads;
    private final ArrayQueue<Runnable> queue=new ArrayQueue<>();

    public DaemonThreadPool(int threadCount, String name,int hlt) {
        this.threadCount = threadCount;
        this.threads= new LoopWorker[threadCount];
        for (int i=0;i<threadCount;i++){
            threads[i]=new LoopWorker(this.queue,hlt);
            new Thread(threads[i],name+"-"+i).start();
            i++;
        }
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void submit(Runnable r){
        this.queue.add(r);
    }

    public void shutdown(){
        for (LoopWorker worker:this.threads){
            worker.setRunning(false);
        }
    }
}
