//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.lwjglx.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.windows.GLFWWin32;
import org.lwjglx.BufferUtils;
import org.lwjglx.LWJGLException;
import org.lwjglx.Sys;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;

public class Display {
    private static String windowTitle = "Game";
    private static GLContext context;
    private static boolean displayCreated = false;
    private static boolean displayFocused = false;
    private static boolean displayVisible = true;
    private static boolean displayDirty = false;
    private static boolean displayResizable = false;
    private static DisplayMode mode = new DisplayMode(640, 480);
    private static DisplayMode desktopDisplayMode = new DisplayMode(640, 480);
    private static int latestEventKey = 0;
    private static int displayX = 0;
    private static int displayY = 0;
    private static boolean displayResized = false;
    private static int displayWidth = 0;
    private static int displayHeight = 0;
    private static int displayFramebufferWidth = 0;
    private static int displayFramebufferHeight = 0;
    private static boolean latestResized = false;
    private static int latestWidth = 0;
    private static int latestHeight = 0;
    private static boolean fullScreen;

    public Display() {
    }

    public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
        System.out.println("TODO: Implement Display.create(PixelFormat, Drawable)");
        create();
    }

    public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
        System.out.println("TODO: Implement Display.create(PixelFormat, ContextAttribs)");
        create();
    }

    public static void create(PixelFormat pixel_format,boolean fullScreen) throws LWJGLException {
        long monitor = fullScreen ? GLFW.glfwGetPrimaryMonitor() : 0;
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        int monitorWidth = GLFWvidmode.width(vidmode);
        int monitorHeight = GLFWvidmode.height(vidmode);
        int monitorBitPerPixel = GLFWvidmode.redBits(vidmode) + GLFWvidmode.greenBits(vidmode) + GLFWvidmode.blueBits(vidmode);
        int monitorRefreshRate = GLFWvidmode.refreshRate(vidmode);
        desktopDisplayMode = new DisplayMode(monitorWidth, monitorHeight, monitorBitPerPixel, monitorRefreshRate);
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(131076, 0);
        GLFW.glfwWindowHint(131075, displayResizable ? 1 : 0);
        GLFW.glfwWindowHint(139271, 1);
        GLFW.glfwWindowHint(135181, pixel_format.getSamples());
        Display.Window.handle = GLFW.glfwCreateWindow(mode.getWidth(), mode.getHeight(), windowTitle, monitor, 0L);
        if (Display.Window.handle == 0L) {
            throw new IllegalStateException("Failed to create Display window");
        } else {
            Display.Window.keyCallback = new GLFWKeyCallback() {
                public void invoke(long window, int key, int scancode, int action, int mods) {
                    Display.latestEventKey = key;
                    if (action == 0 || action == 1) {
                        Keyboard.addKeyEvent(key, action == 1);
                    }

                }
            };
            Display.Window.charCallback = new GLFWCharCallback() {
                public void invoke(long window, int codepoint) {
                    Keyboard.addCharEvent(Display.latestEventKey, (char)codepoint);
                }
            };
            Display.Window.cursorPosCallback = new GLFWCursorPosCallback() {
                public void invoke(long window, double xpos, double ypos) {
                    Mouse.addMoveEvent(xpos, ypos);
                }
            };
            Display.Window.mouseButtonCallback = new GLFWMouseButtonCallback() {
                public void invoke(long window, int button, int action, int mods) {
                    Mouse.addButtonEvent(button, action == 1);
                }
            };
            Display.Window.windowFocusCallback = new GLFWWindowFocusCallback() {
                public void invoke(long window, int focused) {
                    Display.displayFocused = focused == 1;
                }
            };
            Display.Window.windowIconifyCallback = new GLFWWindowIconifyCallback() {
                public void invoke(long window, int iconified) {
                    Display.displayVisible = iconified == 0;
                }
            };
            Display.Window.windowSizeCallback = new GLFWWindowSizeCallback() {
                public void invoke(long window, int width, int height) {
                    Display.latestResized = true;
                    Display.latestWidth = width;
                    Display.latestHeight = height;
                }
            };
            Display.Window.windowPosCallback = new GLFWWindowPosCallback() {
                public void invoke(long window, int xpos, int ypos) {
                    Display.displayX = xpos;
                    Display.displayY = ypos;
                }
            };
            Display.Window.windowRefreshCallback = new GLFWWindowRefreshCallback() {
                public void invoke(long window) {
                    Display.displayDirty = true;
                }
            };
            Display.Window.framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
                public void invoke(long window, int width, int height) {
                    Display.displayFramebufferWidth = width;
                    Display.displayFramebufferHeight = height;
                }
            };
            Display.Window.setCallbacks();
            displayWidth = mode.getWidth();
            displayHeight = mode.getHeight();
            IntBuffer fbw = BufferUtils.createIntBuffer(1);
            IntBuffer fbh = BufferUtils.createIntBuffer(1);
            GLFW.glfwGetFramebufferSize(Display.Window.handle, fbw, fbh);
            displayFramebufferWidth = fbw.get(0);
            displayFramebufferHeight = fbh.get(0);
            GLFW.glfwSetWindowPos(Display.Window.handle, (monitorWidth - mode.getWidth()) / 2, (monitorHeight - mode.getHeight()) / 2);
            displayX = (monitorWidth - mode.getWidth()) / 2;
            displayY = (monitorHeight - mode.getHeight()) / 2;
            GLFW.glfwMakeContextCurrent(Display.Window.handle);
            context = GLContext.createFromCurrent();
            GLFW.glfwSwapInterval(1);
            GLFW.glfwShowWindow(Display.Window.handle);
            displayCreated = true;
        }
    }

    public static void create() throws LWJGLException {
        create((new PixelFormat()).withSamples(1),false);
    }

    public static boolean isCreated() {
        return displayCreated;
    }

    public static boolean isActive() {
        return displayFocused;
    }

    public static boolean isVisible() {
        return displayVisible;
    }

    public static GLContext getContext() {
        return context;
    }

    public static void setLocation(int new_x, int new_y) {
        System.out.println("TODO: Implement Display.setLocation(int, int)");
    }

    public static void setVSyncEnabled(boolean sync) {
        System.out.println("TODO: Implement Display.setVSyncEnabled(boolean)");
    }

    public static long getWindow() {
        return Display.Window.handle;
    }

    public static void update() {
        update(true);
    }

    public static void update(boolean processMessages) {
        try {
            swapBuffers();
            displayDirty = false;
        } catch (LWJGLException var2) {
            throw new RuntimeException(var2);
        }

        if (processMessages) {
            processMessages();
        }
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

    public static void swapBuffers() throws LWJGLException {
        GLFW.glfwSwapBuffers(Display.Window.handle);
    }

    public static void destroy() {
        Display.Window.releaseCallbacks();
        GLFW.glfwDestroyWindow(Display.Window.handle);
        displayCreated = false;
    }

    public static void setDisplayMode(DisplayMode dm) throws LWJGLException {
        mode = dm;
    }

    public static DisplayMode getDisplayMode() {
        return mode;
    }

    public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        IntBuffer count = BufferUtils.createIntBuffer(1);
        ByteBuffer modes = GLFW.glfwGetVideoModes(GLFW.glfwGetPrimaryMonitor(), count);
        DisplayMode[] displayModes = new DisplayMode[count.get(0)];

        for(int i = 0; i < count.get(0); ++i) {
            modes.position(i * GLFWvidmode.SIZEOF);
            int w = GLFWvidmode.width(modes);
            int h = GLFWvidmode.height(modes);
            int b = GLFWvidmode.redBits(modes) + GLFWvidmode.greenBits(modes) + GLFWvidmode.blueBits(modes);
            int r = GLFWvidmode.refreshRate(modes);
            displayModes[i] = new DisplayMode(w, h, b, r);
        }

        return displayModes;
    }

    public static DisplayMode getDesktopDisplayMode() {
        return desktopDisplayMode;
    }

    public static boolean wasResized() {
        return displayResized;
    }

    public static int getX() {
        return displayX;
    }

    public static int getY() {
        return displayY;
    }

    public static int getWidth() {
        IntBuffer ib=BufferUtils.createIntBuffer(1);
        IntBuffer ib2=BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(Window.handle,ib,ib2);
        return ib.get();
    }

    public static int getHeight() {
        IntBuffer ib=BufferUtils.createIntBuffer(1);
        IntBuffer ib2=BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(Window.handle,ib,ib2);
        return ib2.get();
    }

    public static int getFramebufferWidth() {
        return displayFramebufferWidth;
    }

    public static int getFramebufferHeight() {
        return displayFramebufferHeight;
    }

    public static void setTitle(String title) {
        windowTitle = title;
        //GLFW.glfwSetWindowTitle(Window.handle,windowTitle);
    }

    public static boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(Display.Window.handle) == 1;
    }

    public static boolean isDirty() {
        return displayDirty;
    }

    public static void setInitialBackground(float red, float green, float blue) {
        GL11.glClearColor(red,green,blue,1.0f);
    }

    public static int setIcon(ByteBuffer[] icons) {
        System.out.println("TODO: Implement Display.setIcon(ByteBuffer[])");
        return 0;
    }

    public static void setResizable(boolean resizable) {
        displayResizable = resizable;
    }

    public static boolean isResizable() {
        return displayResizable;
    }

    public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
        System.out.println("TODO: Implement Display.setDisplayModeAndFullscreen(DisplayMode)");
    }

    public static void setFullscreen(boolean fullscreen) throws LWJGLException {
        //todo:fixthat fucking glfw,fuckyou glfw!!!!!!!!!!!!!!!!!
        fullScreen=fullscreen;
        if(fullscreen){
            GLFW.glfwSetWindowSize(Window.handle,desktopDisplayMode.getWidth(),desktopDisplayMode.getHeight());
            GLFW.glfwSetWindowPos(Window.handle,0,0);
            GLFW.glfwWindowHint(GLFW.GLFW_FLOATING,1);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,0);
        }else{
            GLFW.glfwSetWindowSize(Window.handle,displayWidth,displayHeight);
            GLFW.glfwSetWindowPos(Window.handle,displayX,displayY);
            GLFW.glfwWindowHint(GLFW.GLFW_FLOATING,0);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,1);
        }
    }

    public static boolean isFullscreen() {
        return fullScreen;
    }

    public static void setParent(Canvas parent) throws LWJGLException {
    }

    public static void releaseContext() throws LWJGLException {
        GLFW.glfwMakeContextCurrent(0L);
    }

    public static boolean isCurrent() throws LWJGLException {
        return context.isCurrent();
    }

    public static void makeCurrent() throws LWJGLException {
        GLFW.glfwMakeContextCurrent(Display.Window.handle);
    }

    public static String getAdapter() {
        return "GeNotSupportedAdapter";
    }

    public static String getVersion() {
        return "1.0 NOT SUPPORTED";
    }

    public static void sync(int fps) {
        Sync.sync(fps);
    }

    public static Drawable getDrawable() {
        return null;
    }

    static DisplayImplementation getImplementation() {
        return null;
    }

    static {
        Sys.initialize();
        long monitor = GLFW.glfwGetPrimaryMonitor();
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(monitor);
        int monitorWidth = GLFWvidmode.width(vidmode);
        int monitorHeight = GLFWvidmode.height(vidmode);
        int monitorBitPerPixel = GLFWvidmode.redBits(vidmode) + GLFWvidmode.greenBits(vidmode) + GLFWvidmode.blueBits(vidmode);
        int monitorRefreshRate = GLFWvidmode.refreshRate(vidmode);
        desktopDisplayMode = new DisplayMode(monitorWidth, monitorHeight, monitorBitPerPixel, monitorRefreshRate);
    }

    private static class Window {
        static long handle;
        static GLFWKeyCallback keyCallback;
        static GLFWCharCallback charCallback;
        static GLFWCursorPosCallback cursorPosCallback;
        static GLFWMouseButtonCallback mouseButtonCallback;
        static GLFWWindowFocusCallback windowFocusCallback;
        static GLFWWindowIconifyCallback windowIconifyCallback;
        static GLFWWindowSizeCallback windowSizeCallback;
        static GLFWWindowPosCallback windowPosCallback;
        static GLFWWindowRefreshCallback windowRefreshCallback;
        static GLFWFramebufferSizeCallback framebufferSizeCallback;

        private Window() {
        }

        public static void setCallbacks() {
            Callbacks.glfwSetCallback(handle, keyCallback);
            Callbacks.glfwSetCallback(handle, charCallback);
            Callbacks.glfwSetCallback(handle, cursorPosCallback);
            Callbacks.glfwSetCallback(handle, mouseButtonCallback);
            Callbacks.glfwSetCallback(handle, windowFocusCallback);
            Callbacks.glfwSetCallback(handle, windowIconifyCallback);
            Callbacks.glfwSetCallback(handle, windowSizeCallback);
            Callbacks.glfwSetCallback(handle, windowPosCallback);
            Callbacks.glfwSetCallback(handle, windowRefreshCallback);
            Callbacks.glfwSetCallback(handle, framebufferSizeCallback);
        }

        public static void releaseCallbacks() {
            keyCallback.release();
            charCallback.release();
            cursorPosCallback.release();
            mouseButtonCallback.release();
            windowFocusCallback.release();
            windowIconifyCallback.release();
            windowSizeCallback.release();
            windowPosCallback.release();
            windowRefreshCallback.release();
            framebufferSizeCallback.release();
        }
    }
}
