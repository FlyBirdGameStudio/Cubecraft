package com.flybirdstudio.util.task;

import com.flybirdstudio.util.math.MathHelper;

public class LoadTask implements Runnable{
    private final int loopTime;
    private final float startProg;
    private final float endProg;
    private final LoadTaskOperation loadTaskOperation;
    private float previousProgress;
    private final TaskProgressUpdateListener listener;

    public LoadTask(int loopTime, float startProgress, float endProgress, LoadTaskOperation loadTaskOperation, TaskProgressUpdateListener listener){
        this.loopTime=loopTime;
        this.startProg=startProgress;
        this.endProg=endProgress;
        this.loadTaskOperation=loadTaskOperation;
        this.listener = listener;
    }

    @Override
    public void run() {
        for (int i=0;i<loopTime;i++){
            this.loadTaskOperation.operation(i);
            this.previousProgress= (float) MathHelper.scale(i,startProg,endProg,0,loopTime);
            this.listener.onProgressChange((int) previousProgress);
        }
    }
}
