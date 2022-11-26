//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.flybird.starfish3d.platform;


import org.lwjgl.glfw.GLFW;

class Sync {
    private static final long NANOS_IN_SECOND = 1000000000L;
    private static long nextFrame = 0L;
    private static boolean initialised = false;
    private static RunningAvg sleepDurations = new RunningAvg(10);
    private static RunningAvg yieldDurations = new RunningAvg(10);

    Sync() {
    }

    public static void sync(int fps) {
        if (fps > 0) {
            if (!initialised) {
                initialise();
            }

            try {
                long t0;
                long t1;
                for(t0 = getTime(); nextFrame - t0 > sleepDurations.avg(); t0 = t1) {
                    Thread.sleep(1L);
                    sleepDurations.add((t1 = getTime()) - t0);
                }

                sleepDurations.dampenForLowResTicker();

                for(t0 = getTime(); nextFrame - t0 > yieldDurations.avg(); t0 = t1) {
                    Thread.yield();
                    yieldDurations.add((t1 = getTime()) - t0);
                }
            } catch (InterruptedException var5) {
            }

            nextFrame = Math.max(nextFrame + 1000000000L / (long)fps, getTime());
        }
    }

    private static void initialise() {
        initialised = true;
        sleepDurations.init(1000000L);
        yieldDurations.init((long)((int)((double)(-(getTime() - getTime())) * 1.333)));
        nextFrame = getTime();
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Win")) {
            Thread timerAccuracyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (Exception var2) {}
                }
            });
            timerAccuracyThread.setName("LWJGL Timer");
            timerAccuracyThread.setDaemon(true);
            timerAccuracyThread.start();
        }

    }

    private static long getTime() {
        return (long) (GLFW.glfwGetTime()*1000 * 1000000000L / 1000L);
    }
}
