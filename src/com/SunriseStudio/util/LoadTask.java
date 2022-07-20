package com.sunrisestudio.util;

import com.sunrisestudio.util.math.MathHelper;

public class LoadTask implements Runnable{
    private final int loopTime;
    private final float startProg;
    private final float endProg;
    private final LoadTaskOperation loadTaskOperation;
    private float previousProgress;

    public LoadTask(int loopTime,float startProgress,float endProgress,LoadTaskOperation loadTaskOperation){
        this.loopTime=loopTime;
        this.startProg=startProgress;
        this.endProg=endProgress;
        this.loadTaskOperation=loadTaskOperation;
    }

    @Override
    public void run() {
        for (int i=0;i<loopTime;i++){
            this.loadTaskOperation.operation(i);
            this.previousProgress= (float) MathHelper.scale(i,startProg,endProg,0,loopTime);
        }
    }

    public float getPreviousProgress() {
        return previousProgress;
    }
}
