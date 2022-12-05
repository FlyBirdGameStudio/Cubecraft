package io.flybird.starfish3d.render.multiThread;

import io.flybird.util.container.ArrayQueue;
import io.flybird.util.logging.LogHandler;

public class VertexArrayCompilerCall<T extends VertexArrayCompileCallable> implements Runnable {
    public T compileCallable;
    public ArrayQueue<DrawCompile> queue;
    public ArrayQueue<IDrawCompile> allQueue;
    private LogHandler logger=LogHandler.create("Starfish3D/VertexArrayCompileCall");

    public VertexArrayCompilerCall(T compileCallable, ArrayQueue<DrawCompile> queue, ArrayQueue<IDrawCompile> allQueue) {
        this.compileCallable = compileCallable;
        this.queue = queue;
        this.allQueue = allQueue;
    }

    @Override
    public void run() {
        try {
            IDrawCompile[] res = this.compileCallable.compile();
            for (IDrawCompile dc : res) {
                if (dc instanceof DrawCompile) {
                    this.queue.add((DrawCompile) dc);
                }
                this.allQueue.add(dc);
            }
        }catch (Exception e){
            this.logger.exception(e);
        }
    }
}
