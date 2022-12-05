package io.flybird.util.task;

import io.flybird.util.container.ArrayQueue;

public class LoopWorker implements Runnable{
    private final ArrayQueue<Runnable> queue;
    private boolean running;
    private final long haltTime;

    public LoopWorker(ArrayQueue<Runnable> queue, long haltTime) {
        this.queue = queue;
        this.haltTime = haltTime;
        this.running=true;
    }

    @Override
    public void run() {
        while (running){
            if(this.queue.size()>0) {
                Runnable t = this.queue.poll();
                if (t != null) {
                    t.run();
                }
            }else{
                try {
                    Thread.sleep(this.haltTime);
                } catch (InterruptedException e) {
                    //do nth
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
