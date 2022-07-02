package com.SunriseStudio.cubecraft.util.timer;



public class TimerQueueEvent {
    public TimerEvent event;
    public long lastUpdateTime;
    public long remainingTime;
    public boolean isPeriod;
    public long latency;
}
