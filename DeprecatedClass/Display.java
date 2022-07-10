package com.sunrisestudio.grass3d.platform;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GLContext;
import org.lwjglx.BufferUtils;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.DisplayMode;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Display {
    private static boolean created;
    private static long handle;

    private static GLFWErrorCallback errorCallback;
    private static GLFWMonitorCallback monitorCallback;
    private static GLFWWindowPosCallback windowPosCallback;
    private static GLFWWindowFocusCallback windowFocusCallback;
    private static GLFWWindowIconifyCallback windowIconifyCallback;
    private static GLFWFramebufferSizeCallback framebufferSizeCallback;
    private static GLFWWindowSizeCallback windowSizeCallback;
    private static GLFWWindowRefreshCallback windowRefreshCallback;

    private static boolean visible;
    private static boolean focused;
    private static boolean dirty;

    private static int fboWidth, fboHeight;
    private static int x, y;
    private static int width=854, height=480;
    private static boolean latestResized;
    private static String title="Grass3D window";

    private static void initCallBacks() {
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int w, int h) {
                latestResized = true;
                width = w;
                height = h;
            }
        };
        framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                fboWidth = width;
                fboHeight = height;
            }
        };
        windowFocusCallback = new GLFWWindowFocusCallback() {
            public void invoke(long window, int focus) {
                focused = focus == 1;
            }
        };
        windowIconifyCallback = new GLFWWindowIconifyCallback() {
            public void invoke(long window, int iconified) {
                visible = iconified == 0;
            }
        };
        windowRefreshCallback = new GLFWWindowRefreshCallback() {
            public void invoke(long window) {
                dirty = true;
            }
        };
        windowPosCallback = new GLFWWindowPosCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                x = i;
                y = i1;
            }
        };
        monitorCallback = new GLFWMonitorCallback() {
            @Override
            public void invoke(long l, int i) {

            }
        };
        errorCallback = new GLFWErrorCallback() {
            @Override
            public void invoke(int i, long l) {

            }
        };
    }

    private static void setDisplayCallback(){
        GLFW.glfwSetErrorCallback(errorCallback);
        GLFW.glfwSetMonitorCallback(monitorCallback);
        GLFW.glfwSetWindowPosCallback(handle,windowPosCallback);
        GLFW.glfwSetWindowFocusCallback(handle,windowFocusCallback);
        GLFW.glfwSetWindowIconifyCallback(handle,windowIconifyCallback);
        GLFW.glfwSetFramebufferSizeCallback(handle,framebufferSizeCallback);
        GLFW.glfwSetWindowSizeCallback(handle,windowSizeCallback);
        GLFW.glfwSetWindowRefreshCallback(handle,windowRefreshCallback);
    }

    private static void releaseWindowCallback() {
        errorCallback.release();
        monitorCallback.release();
        framebufferSizeCallback.release();
        windowPosCallback.release();
        windowFocusCallback.release();
        windowIconifyCallback.release();
        windowSizeCallback.release();
        windowRefreshCallback.release();
    }

    public static void create() {
        //get window handle
        handle = GLFW.glfwCreateWindow(width,height, title, 0L, 0L);
        if (handle == 0L) {
            throw new IllegalStateException("Failed to create Display window");
        }

        //set window attribute
        GLFW.glfwDefaultWindowHints();
        initCallBacks();
        setDisplayCallback();

        displayWidth = mode.getWidth();
        displayHeight = mode.getHeight();
        getDisplaySize();
        GLFW.glfwSetWindowPos(handle, (monitorWidth - mode.getWidth()) / 2, (monitorHeight - mode.getHeight()) / 2);
        displayX = (monitorWidth - mode.getWidth()) / 2;
        displayY = (monitorHeight - mode.getHeight()) / 2;
        GLFW.glfwMakeContextCurrent(org.lwjglx.opengl.Display.Window.handle);
        context = GLContext.createFromCurrent();
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(org.lwjglx.opengl.Display.Window.handle);
        created = true;





            org.lwjglx.opengl.Display.Window.keyCallback = new GLFWKeyCallback() {
                public void invoke(long window, int key, int scancode, int action, int mods) {
                    org.lwjglx.opengl.Display.latestEventKey = key;
                    if (action == 0 || action == 1) {
                        Keyboard.addKeyEvent(key, action == 1);
                    }

                }
            };
            org.lwjglx.opengl.Display.Window.charCallback = new GLFWCharCallback() {
                public void invoke(long window, int codepoint) {
                    Keyboard.addCharEvent(org.lwjglx.opengl.Display.latestEventKey, (char) codepoint);
                }
            };
            org.lwjglx.opengl.Display.Window.cursorPosCallback = new GLFWCursorPosCallback() {
                public void invoke(long window, double xpos, double ypos) {
                    Mouse.addMoveEvent(xpos, ypos);
                }
            };
            org.lwjglx.opengl.Display.Window.mouseButtonCallback = new GLFWMouseButtonCallback() {
                public void invoke(long window, int button, int action, int mods) {
                    Mouse.addButtonEvent(button, action == 1);
                }
            };

    }

    public static void processMessages() {
        GLFW.glfwPollEvents();
        Keyboard.poll();
        Mouse.poll();
        if (latestResized) {
            latestResized = false;
            displayResized = true;
            displayWidth = latestWidth;
            displayHeight = latestHeight;
        } else {
            displayResized = false;
        }
    }





    private static void getDisplaySize(){
        IntBuffer fbw = BufferUtils.createIntBuffer(1);
        IntBuffer fbh = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(handle, fbw, fbh);
        fboWidth = fbw.get(0);
        fboHeight = fbh.get(0);
    }

    private static MonitorInfo getMonitorInfo(){
        long monitor = GLFW.glfwGetPrimaryMonitor();
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(monitor);
        int monitorWidth = GLFWvidmode.width(vidmode);
        int monitorHeight = GLFWvidmode.height(vidmode);
        int monitorBitPerPixel = GLFWvidmode.redBits(vidmode) + GLFWvidmode.greenBits(vidmode) + GLFWvidmode.blueBits(vidmode);
        int monitorRefreshRate = GLFWvidmode.refreshRate(vidmode);
        return new MonitorInfo(monitorWidth,monitorHeight,monitorBitPerPixel,monitorRefreshRate);
    }



    public static void setSample(int FXAALevel){
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,FXAALevel);
    }

    public static void setTitle(String title) {

    }

    public static long getHandle() {
        return handle;
    }
}
