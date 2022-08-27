
package com.sunrisestudio.grass3d.platform.input;

import com.sunrisestudio.grass3d.platform.Callbacks;
import com.sunrisestudio.grass3d.platform.Display;
import org.lwjgl.glfw.*;


public class Mouse {
    private static boolean grabbed = false;
    private static int lastX = 0;
    private static int lastY = 0;
    private static int latestX = 0;
    private static int latestY = 0;
    private static int x = 0;
    private static int y = 0;
    private static EventQueue queue = new EventQueue(32);
    private static int[] buttonEvents = new int[queue.getMaxEvents()];;
    private static boolean[] buttonEventStates= new boolean[queue.getMaxEvents()];
    private static int[] xEvents = new int[queue.getMaxEvents()];
    private static int[] yEvents = new int[queue.getMaxEvents()];
    private static int[] lastxEvents = new int[queue.getMaxEvents()];
    private static int[] lastyEvents = new int[queue.getMaxEvents()];
    private static long[] nanoTimeEvents = new long[queue.getMaxEvents()];
    private static boolean clipPostionToDisplay=true;
    static final double[] scroll = new double[]{0.0};
    static GLFWScrollCallback callback;

    public static void addMoveEvent(double mouseX, double mouseY) {
        latestX = (int)mouseX;
        latestY = Display.getHeight() - (int)mouseY;
        lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
        lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];
        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;
        buttonEvents[queue.getNextPos()] = -1;
        buttonEventStates[queue.getNextPos()] = false;
        nanoTimeEvents[queue.getNextPos()] = System.nanoTime();
        queue.add();
    }

    public static void addButtonEvent(int button, boolean pressed) {
        lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
        lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];
        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;
        buttonEvents[queue.getNextPos()] = button;
        buttonEventStates[queue.getNextPos()] = pressed;
        nanoTimeEvents[queue.getNextPos()] = System.nanoTime();
        queue.add();
    }

    public static void poll() {
        lastX = x;
        lastY = y;
        if (!grabbed && clipPostionToDisplay) {
            if (latestX < 0) {
                latestX = 0;
            }

            if (latestY < 0) {
                latestY = 0;
            }

            if (latestX > Display.getWidth() - 1) {
                latestX = Display.getWidth() - 1;
            }

            if (latestY > Display.getHeight() - 1) {
                latestY = Display.getHeight() - 1;
            }
        }

        x = latestX;
        y = latestY;
    }


    public static void setGrabbed(boolean grab) {
        GLFW.glfwSetInputMode(Display.getHandle(), 208897, grab ? 212995 : 212993);
        grabbed = grab;
    }

    public static boolean isGrabbed() {
        return grabbed;
    }

    public static boolean isButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Display.getHandle(), button) == 1;
    }

    public static boolean next() {
        return queue.next();
    }

    public static int getEventX() {
        return xEvents[queue.getCurrentPos()];
    }

    public static int getEventY() {
        return yEvents[queue.getCurrentPos()];
    }

    public static int getEventDX() {
        return xEvents[queue.getCurrentPos()] - lastxEvents[queue.getCurrentPos()];
    }

    public static int getEventDY() {
        return yEvents[queue.getCurrentPos()] - lastyEvents[queue.getCurrentPos()];
    }

    public static long getEventNanoseconds() {
        return nanoTimeEvents[queue.getCurrentPos()];
    }

    public static int getEventButton() {
        return buttonEvents[queue.getCurrentPos()];
    }

    public static boolean getEventButtonState() {
        return buttonEventStates[queue.getCurrentPos()];
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getDX() {
        return x - lastX;
    }

    public static int getDY() {
        return y - lastY;
    }

    public static int getDWheel() {
        int a = (int)(scroll[0]);
        scroll[0] = 0.0;
        return a;
    }

    public static int getButtonCount() {
        return 8;
    }

    public static void setClipMouseCoordinatesToWindow(boolean clip) {
        clipPostionToDisplay = clip;
    }

    public static void setCursorPosition(int new_x, int new_y) {
        GLFW.glfwSetCursorPos(Display.getHandle(), (double)new_x, (double)new_y);
    }

    public static void initCallbacks() {
        Callbacks.cursorPosCallback = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                Mouse.addMoveEvent(xpos, ypos);
            }
        };
        Callbacks.mouseButtonCallback = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                Mouse.addButtonEvent(button, action == 1);
            }
        };
        Callbacks.scrollCallback=new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                scroll[0]=-v1;
            }
        };
    }
}
