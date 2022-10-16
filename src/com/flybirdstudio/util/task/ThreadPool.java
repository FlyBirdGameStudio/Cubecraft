package com.flybirdstudio.util.task;

import com.flybirdstudio.util.container.ArrayQueue;

public class ThreadPool {
    private final int threadCount;
    private final Thread[] threads;
    private final boolean[] usage;
    private final String name;
    private boolean running=true;
    private ArrayQueue<Runnable> queue=new ArrayQueue<>();

    public ThreadPool(int threadCount, String name) {
        this.threadCount = threadCount;
        this.threads=new Thread[threadCount];
        this.usage = new boolean[threadCount];
        this.name=name;
        this.activate();
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void submit(Runnable r){
        if(!running){
            this.activate();
        }
        this.queue.add(r);
    }

    public void shutdown(){
        this.running=false;
    }

    //todo:fix stuck bug
    public void activate(){
        this.running=true;
        new Thread(() -> {
            while (running){
                for (int i=0;i<threadCount;i++){
                    if(queue.size()>0&&!usage[i]){
                        int finalI = i;
                        threads[i]=new Thread(() -> {
                            usage[finalI]=true;
                            queue.poll().run();
                            usage[finalI]=false;
                        },name+"/worker-"+finalI);
                        threads[i].start();
                    }
                }
            }
        },name+"-daemon").start();
    }
}
