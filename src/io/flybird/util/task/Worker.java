package io.flybird.util.task;

import io.flybird.util.LogHandler;
import io.flybird.util.container.ArrayQueue;

public class Worker<I> implements Runnable{
    public final ArrayQueue<I> queue;
    public final WorkerTask<I> callable;
    public LogHandler logger;

    protected Worker(ArrayQueue<I> queue, WorkerTask<I> callable) {
        this.queue = queue;
        this.callable = callable;
        this.logger=LogHandler.create("WorldGenerator-Worker:"+this.callable.getID(),"client");
    }

    @Override
    public void run() {
        try {
            this.queue.add(this.callable.call());
        } catch (Exception e) {
            logger.exception(e);
        }
    }
}
