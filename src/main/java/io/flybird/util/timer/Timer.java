package io.flybird.util.timer;

import io.flybird.util.math.MathHelper;

public class Timer{
	private final float ticksPerSecond;
	private long lastTime;
	public int ticks;
	public float interpolatedTime;
	public final float timeScale;
	public float passedTime;
	public final float tps;
	public final float speed;

	public Timer(final float ticksPerSecond) {
		this.timeScale = 1.0f;
		this.passedTime = 0.0f;
		this.ticksPerSecond = ticksPerSecond;
		this.lastTime = System.nanoTime();
		this.speed = 1.0f;
		this.tps = ticksPerSecond;
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
		this.passedTime += passedNs * this.timeScale * this.ticksPerSecond / 1.0E9f;
		this.ticks = (int)this.passedTime;
		this.ticks= (int) MathHelper.clamp(this.ticks,100,0);
		this.passedTime -= this.ticks;
		this.interpolatedTime = this.passedTime;
	}


	public static long last;
	public static void startTiming(){
		last=System.currentTimeMillis();
	}

	public static long endTiming(){
		return System.currentTimeMillis()-last;
	}
}
