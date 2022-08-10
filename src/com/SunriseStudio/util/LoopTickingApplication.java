package com.sunrisestudio.util;

import com.sunrisestudio.util.timer.Timer;

public abstract class LoopTickingApplication implements Runnable{
    protected LogHandler logHandler;
    protected Timer timer;
    protected boolean running;
    private TimingInfo timingInfo;

    public void init()throws Exception {}
    public void shortTick() {}
    public void longTick(){}
    public void stop(){}
    public void on1sec(){}


    @Override
    public void run() {
        this.running = true;

        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
            this.running = false;
            this.stop();
        }

        long lastTime = System.currentTimeMillis();

        int longTicks=0,shortTicks=0,longTickDuration=0,shortTickDuration=0;

        try {
            while (this.running) {
                long last_0=System.currentTimeMillis();
                this.shortTick();
                shortTickDuration= (int) (System.currentTimeMillis()-last_0);

                timer.advanceTime();
                shortTicks++;
                for (int i = 0; i < timer.ticks; ++i) {
                    long last_1=System.currentTimeMillis();
                    this.longTick();
                    longTickDuration= (int) (System.currentTimeMillis()-last_1);
                    longTicks++;
                }
                while (System.currentTimeMillis() >= lastTime + 1000L) {
                    try {
                        this.timingInfo=new TimingInfo(
                                shortTicks, shortTickDuration,
                                longTicks, longTickDuration
                        );
                    }catch (ArithmeticException ignored){}
                    shortTicks=0;
                    longTicks=0;
                    lastTime += 1000L;

                    this.on1sec();
                }
            }
        } catch (Exception e) {
            logHandler.exception(e);
            e.printStackTrace();
            logHandler.info("find exception!stopping...");
            this.running = false;
            this.stop();
        } finally {
            this.running = false;
            this.stop();
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public record TimingInfo(int shortTickTPS, int shortTickMSPT, int longTickTPS, int longTickMSPT){}

    public TimingInfo getTimingInfo() {
        return timingInfo;
    }
}
