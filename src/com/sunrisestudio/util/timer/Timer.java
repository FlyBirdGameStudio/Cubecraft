package com.sunrisestudio.util.timer;

import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.util.math.MathHelper;

import java.util.HashMap;

public class Timer
{
	private final float ticksPerSecond;
	public float elapsedDelta;
	private long lastTime;
	public int ticks;
	public float interpolatedTime;
	public float timeScale;
	public float fps;
	public float passedTime;

	public float tps;
	public float speed;
	long lastSysClock;
	long lastHRClock;
	double adjustment;

	private final HashMap<String,TimerTask> freq=new HashMap<>();
	private final HashMap<String,TimerTask> delay=new HashMap<>();
	private long lastTimeMills=System.currentTimeMillis();
	private long passedTimeMills=0;

	public Timer(final float ticksPerSecond) {
		this.timeScale = 1.0f;
		this.fps = 0.0f;
		this.passedTime = 0.0f;
		this.ticksPerSecond = ticksPerSecond;
		this.lastTime = System.nanoTime();
		this.speed = 1.0f;
		this.elapsedDelta = 0.0f;
		this.adjustment = 1.0;
		this.tps = ticksPerSecond;
		this.lastSysClock = System.currentTimeMillis();
		this.lastHRClock = System.nanoTime() / 1000000L;
	}

	public void advanceTime() {
		final long now = System.nanoTime();
		long passedNs = now - this.lastTime;
		this.lastTime = now;
		if (passedNs < 1L) {
			passedNs = 1L;
		}
		if (passedNs > 1000000000L) {
			passedNs = 1000000000L;
		}
		this.fps = (float)(1000000000L / passedNs);
		this.passedTime += passedNs * this.timeScale * this.ticksPerSecond / 1.0E9f;
		this.ticks = (int)this.passedTime;
		this.ticks= (int) MathHelper.clamp(this.ticks,100,0);
		this.passedTime -= this.ticks;
		this.interpolatedTime = this.passedTime;


		CollectionUtil.iterateMap(this.delay, (key, item) -> {
			item.remainingTime-=passedTimeMills;
			if(item.remainingTime<=0){
				item.run();
			}
		});
	}

	public void runTaskFrequent(TimerTask task,long freq,long time,long delay){

	}

	public void runTaskDelay(TimerTask task,long delay){

	}

	public static long last;
	public static void startTiming(){
		last=System.currentTimeMillis();
	}

	public static long endTiming(){
		return System.currentTimeMillis()-last;
	}
}
