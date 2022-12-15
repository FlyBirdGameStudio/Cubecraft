package io.flybird.starfish3d.render.multiThread;

import io.flybird.util.container.ArrayQueue;
import io.flybird.util.logging.LogHandler;

public class CompilerWorker implements Runnable{
    public final ArrayQueue<DrawCompile> queue;
    public final ArrayQueue<IDrawCompile> allQueue;
    public final ArrayQueue<? extends VertexArrayCompileCallable> src;
    private final LogHandler logger=LogHandler.create("Starfish3D/VertexArrayCompileWorker");

    private boolean running=true;

    public CompilerWorker(ArrayQueue<DrawCompile> queue, ArrayQueue<IDrawCompile> allQueue, ArrayQueue<? extends VertexArrayCompileCallable> src) {
        this.queue = queue;
        this.allQueue = allQueue;
        this.src = src;
    }

    @Override
    public void run() {
        while (running){
            try {
                VertexArrayCompileCallable obj=this.src.poll();
                if(obj!=null) {
                    IDrawCompile[] res = obj.compile();
                    for (IDrawCompile dc : res) {
                        if (dc instanceof DrawCompile) {
                            this.queue.add((DrawCompile) dc);
                        }
                        this.allQueue.add(dc);
                    }
                }else{
                    Thread.yield();
                }
            }catch (Exception e){
                this.logger.exception(e);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
