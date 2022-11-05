package io.flybird.starfish3d.platform;

import io.flybird.util.ImageUtil;
import io.flybird.starfish3d.platform.input.Keyboard;
import io.flybird.starfish3d.platform.input.Mouse;
import io.flybird.util.container.BufferUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Display {
    private static boolean created;
    private static long handle;



    private static boolean visible;
    private static boolean focused;
    private static boolean dirty;

    private static int fboWidth, fboHeight;
    private static int x, y;
    private static int width=1280, height=720;
    private static boolean latestResized;
    private static String title="Grass3D window";
    private static int latestWidth,latestHeight;
    private static boolean resized;
    private static boolean fullScreen;
    private static boolean resizeable;
    private boolean focus;

    private static int lastNFullScreenWidth,lastNFullScreenHeight;
    private static int lastNFullScreenX,lastNFullScreenY;

    private static void initCallBacks() {
        Callbacks.windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int w, int h) {
                latestResized = true;
                latestWidth = w;
                latestHeight = h;
            }
        };
        Callbacks.framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                fboWidth = width;
                fboHeight = height;
            }
        };
        Callbacks.windowFocusCallback = new GLFWWindowFocusCallback() {
            public void invoke(long window, boolean focus) {
                focused = focus;
            }
        };
        Callbacks.windowIconifyCallback = new GLFWWindowIconifyCallback() {
            public void invoke(long window, boolean iconified) {
                visible = iconified;
            }
        };
        Callbacks.windowRefreshCallback = new GLFWWindowRefreshCallback() {
            public void invoke(long window) {
                dirty = true;
            }
        };
        Callbacks.windowPosCallback = new GLFWWindowPosCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                x = i;
                y = i1;
            }
        };
        Callbacks.monitorCallback = new GLFWMonitorCallback() {
            @Override
            public void invoke(long l, int i) {

            }
        };
        Callbacks.errorCallback = new GLFWErrorCallback() {
            @Override
            public void invoke(int i, long l) {
                throw new IllegalStateException(String.valueOf(i));
            }
        };
    }


    public static void update() {
        GLFW.glfwSwapBuffers(handle);
        dirty = false;
        GLFW.glfwPollEvents();
        Keyboard.poll();
        Mouse.poll();
        if (latestResized) {
            latestResized = false;
            resized = true;
            width = latestWidth;
            height = latestHeight;
        } else {
            resized = false;
        }
    }


    public static void create() {
        //get handle
        GLFWErrorCallback.createPrint(System.err).set();

        handle = GLFW.glfwCreateWindow(width,height, title, 0L, 0L);
        if (handle == 0L) {
            throw new IllegalStateException("Failed to create Display window");
        }
        GLFW.glfwDefaultWindowHints();

        //set callback
        initCallBacks();
        Mouse.initCallbacks();
        Keyboard.initCallbacks();
        Callbacks.setCallback(handle);

        //set gl context
        getDisplaySize();
        MonitorInfo monitorInfo=getMonitorInfo();
        x = (monitorInfo.width() - width) / 2;
        y = (monitorInfo.height() - height) / 2;
        GLFW.glfwSetWindowPos(handle, x,y);
        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(handle);
        GL.createCapabilities();
        created = true;
    }

    public static void setVsyncEnable(boolean vsync){
        GLFW.glfwSwapInterval(vsync?1:0);
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
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(monitor);
        return new MonitorInfo(
                vidmode.width(), vidmode.height(),
                vidmode.blueBits()+vidmode.greenBits()+ vidmode.redBits(),
                vidmode.refreshRate()
        );
    }

    public static void setSample(int FXAALevel){
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,FXAALevel);
    }

    public static void setTitle(String title) {
        GLFW.glfwSetWindowTitle(handle,title);
    }

    public static long getHandle() {
        return handle;
    }

    public static void destroy() {
        Callbacks.releaseCallback();
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        created=false;
    }

    public static void setFullscreen(boolean fullscreen) {
        fullScreen=fullscreen;
        if(fullScreen) {
            lastNFullScreenWidth=width;
            lastNFullScreenHeight=height;
            lastNFullScreenX=x;
            lastNFullScreenY=y;
            GLFW.glfwSetWindowMonitor(handle, GLFW.glfwGetPrimaryMonitor(),
                    0,0,
                    getMonitorInfo().width(),getMonitorInfo().height(),getMonitorInfo().freshRate()
            );

        }else{
            GLFW.glfwSetWindowMonitor(handle, 0,
                    lastNFullScreenX,lastNFullScreenY,
                    lastNFullScreenWidth,lastNFullScreenHeight,60
            );
        }
    }

    public static boolean isFullscreen() {
        return fullScreen;
    }

    public static boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public static void setFXAA(int fxaa) {
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,fxaa);
    }

    public static void setResizable(boolean b) {
        resizeable=b;
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,b?1:0);
    }

    public static void sync(int i) {
        Sync.sync(i);
    }

    public static boolean isActive() {
        return focused;
    }

    public static void setIcon(InputStream in)  {
        GLFWImage image = GLFWImage.malloc();
        BufferedImage img;
        try {
            img=ImageIO.read(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer buffer= ImageUtil.getByteFromBufferedImage_RGBA(img);
        image.set(img.getWidth(),img.getHeight(),buffer);
        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);
        GLFW.glfwSetWindowIcon(handle, images);
        images.free();
        image.free();
        BufferUtil.free(buffer);
    }


    public void initCallback(){
        Callbacks.windowFocusCallback = new GLFWWindowFocusCallback() {
            public void invoke(long window, boolean focused) {
                focus=focused;
            }
        };
        Callbacks.windowIconifyCallback = new GLFWWindowIconifyCallback() {
            @Override
            public void invoke(long l, boolean b) {
                visible=b;
            }
        };
        Callbacks.windowSizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int width, int height) {
                latestResized = true;
                latestWidth = width;
                latestHeight = height;
            }
        };
        Callbacks.windowPosCallback = new GLFWWindowPosCallback() {
            public void invoke(long window, int xpos, int ypos) {
                x = xpos;
                y = ypos;
            }
        };
        Callbacks.windowRefreshCallback = new GLFWWindowRefreshCallback() {
            public void invoke(long window) {
                dirty = true;
            }
        };
        Callbacks.framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            public void invoke(long window, int width, int height) {
                fboWidth = width;
                fboHeight = height;
            }
        };
    }

    public static void setSize(int width,int height){
        GLFW.glfwSetWindowSize(handle,width,height);
    }

    public static void setPos(int x,int y){
        GLFW.glfwSetWindowSize(handle,x,y);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void clear(){
        GL11.glClear(16640);
    }

    static {
        GLFW.glfwInit();
    }
}
