package io.flybird.util;

import io.flybird.util.timer.Timer;

public abstract class LoopTickingApplication implements Runnable{
    public boolean isDebug;
    protected LogHandler logHandler=LogHandler.create("main","game");
    protected Timer timer;
    protected boolean running;
    private TimingInfo timingInfo;
    long lastTime = System.currentTimeMillis();

    /**
     * this function initialize your application when thread init.
     * <li>you could initialize everything here.</li>
     * <li>openGL or openAL should be also initialized here.</li>
     */
    public void init(){}

    /**
     * this function runs at a full speed.
     * <li>you could render something here.</li>
     * <li>or do anything thad need full speed looping.</li>
     */
    public void render() {}

    /**
     * this function runs at tick speed(according to your setting).
     * <li>you could make game logic update.</li>
     * <li>or do anything needs tick-speed looping.</li>
     */
    public void tick(){}

    /**
     * game stop logic function.
     * <li>called when exception happened.</li>
     * <li>called when flag"this.running"=false.</li>
     * <li>called when user invocation.</li>
     */
    public void stop(){}

    /**
     * this function called on every second.
     * <li>there might be some time offset caused by timer or lagging.</li>
     * <li>you could update your statistic information.</li>
     */
    public void on1sec(){}



    @Override
    public void run() {
        this.running = true;
        int longTicks=0,shortTicks=0,longTickDuration=0,shortTickDuration;
        try {
            this.init();
            while (this.running) {

                long last_0=System.currentTimeMillis();
                this.render();
                shortTickDuration= (int) (System.currentTimeMillis()-last_0);

                timer.advanceTime();
                shortTicks++;
                for (int i = 0; i < timer.ticks; ++i) {
                    long last_1=System.currentTimeMillis();
                    this.tick();
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
        return timingInfo!=null?timingInfo:new TimingInfo(0,0,0,0);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
