
package io.flybird.starfish3d.platform;

import io.flybird.starfish3d.event.MouseClickEvent;
import io.flybird.starfish3d.event.MouseScrollEvent;
import org.lwjgl.glfw.*;


public class Mouse {
    private static boolean grabbed = false;
    private static int lastX = 0;
    private static int lastY = 0;
    private static int latestX = 0;
    private static int latestY = 0;
    private static int x = 0;
    private static int y = 0;
    private static final EventQueue queue = new EventQueue(32);
    private static final int[] buttonEvents = new int[queue.getMaxEvents()];;
    private static final boolean[] buttonEventStates= new boolean[queue.getMaxEvents()];
    private static final int[] xEvents = new int[queue.getMaxEvents()];
    private static final int[] yEvents = new int[queue.getMaxEvents()];
    private static final int[] lastxEvents = new int[queue.getMaxEvents()];
    private static final int[] lastyEvents = new int[queue.getMaxEvents()];
    static final double[] scroll = new double[]{0.0};

    public static void addMoveEvent(double mouseX, double mouseY) {
        latestX = (int)mouseX;
        latestY = Display.getHeight() - (int)mouseY;
        lastxEvents[queue.getNextPos()] = xEvents[queue.getNextPos()];
        lastyEvents[queue.getNextPos()] = yEvents[queue.getNextPos()];
        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;
        queue.add();
    }


    public static void poll() {
        lastX = x;
        lastY = y;
        if (!grabbed) {
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
                //Mouse.addButtonEvent(button, action == 1);
                if(action!=1){
                    Display.getEventBus().callEvent(new MouseClickEvent(x,y,button));
                }
            }
        };
        Callbacks.scrollCallback=new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                scroll[0]=-v1;
                Display.getEventBus().callEvent(new MouseScrollEvent((int) -v1));
            }
        };
    }
}
