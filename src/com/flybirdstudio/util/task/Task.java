package com.flybirdstudio.util.task;

public abstract class Task extends Thread{
    private final TaskProgressUpdateListener listener;

    public Task(TaskProgressUpdateListener listener) {
        this.listener = listener;
    }

    public Task(String id,TaskProgressUpdateListener listener) {
        super(id);
        this.listener = listener;
    }

    @Override
    public void run() {
        run(this.listener);
    }

    public abstract void run(TaskProgressUpdateListener listener);
}
