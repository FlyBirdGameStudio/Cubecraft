package io.flybird.util.task;

import io.flybird.util.container.ArrayQueue;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiSourceExecution<I> {
    public final HashMap<String,ArrayQueue<I>> queueMap=new HashMap<>();

    public final ExecutorService service;

    public MultiSourceExecution(int threads) {
        this.service = Executors.newFixedThreadPool(threads);
    }

    public void registerNewWorkSource(String id){
        this.queueMap.put(id,new ArrayQueue<>());
    }

    public void unregisterWorkSource(String id){
        this.queueMap.remove(id);
    }

    public ArrayQueue<I> getQueue(String id){
        return this.queueMap.get(id);
    }

    public void submit(WorkerTask<I> task){
        this.service.submit(new Worker<>(getQueue(task.getID()),task));
    }
}
