package com.flybirdstudio.util.timer;

public abstract class TimerTask {
    public long remainingTime;
    private final String id;

    public abstract void run();

    public TimerTask(String id){
        this.id=id;
    }

    public String getId() {
        return id;
    }
}
