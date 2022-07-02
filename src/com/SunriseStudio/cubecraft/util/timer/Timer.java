package com.SunriseStudio.cubecraft.util.timer;

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
		if (this.ticks > 100) {
			this.ticks = 100;
		}
		this.passedTime -= this.ticks;
		this.interpolatedTime = this.passedTime;

	}
}
