package com.SunriseStudio.cubecraft.util;

import com.SunriseStudio.cubecraft.util.timer.Timer;

public abstract class LoopTickingApplication implements Runnable{
    protected LogHandler logHandler;
    protected Timer timer;
    protected boolean running;

    public abstract void init() throws Exception;
    public void shortTick() {}

    public void longTick(){}
    public void stop(){}
    public void on1sec(){}

    protected int shortTickTPS;
    protected int longTickTPS;

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

        int longTicks=0;
        int shortTicks=0;

        try {
            while (this.running) {
                this.shortTick();
                timer.advanceTime();
                shortTicks++;
                for (int i = 0; i < timer.ticks; ++i) {
                    this.longTick();
                    longTicks++;
                }
                while (System.currentTimeMillis() >= lastTime + 1000L) {
                    shortTickTPS=shortTicks;
                    shortTicks=0;
                    longTickTPS=longTicks;
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


    public int getShortTickTPS() {
        return shortTickTPS;
    }

    public int getLongTickTPS() {
        return longTickTPS;
    }

    public boolean isRunning() {
        return this.running;
    }
}
