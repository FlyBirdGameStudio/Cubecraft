package io.flybird.quantum3d.platform;


import io.flybird.quantum3d.BufferAllocation;
import io.flybird.quantum3d.ImageUtil;
import io.flybird.quantum3d.event.*;
import io.flybird.util.event.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class Window {
    //jni glfw
    public static void initGLFW() {
        GLFW.glfwInit();
    }

    public static void destroyGLFW() {
        GLFW.glfwTerminate();
    }



    //window
    public final EventBus windowEvent = new CachedEventBus();
    private long handle;
    private boolean visible;
    private boolean focused;
    private int x, y;
    private int width = 1280, height = 720;
    private boolean latestResized;
    private String title = "Grass3D window";
    private int latestWidth, latestHeight;
    private boolean fullScreen;
    private int lastNFullScreenWidth, lastNFullScreenHeight, lastNFullScreenX, lastNFullScreenY;

    //mouse
    private boolean mouseGrabbed = false;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private int latestX = 0;
    private int latestY = 0;
    private int mouseX = 0;
    private int mouseY = 0;
    static final double[] scroll = new double[]{0.0};

    //keyboard
    boolean keyDownStatus = true;
    int keyDownCount = 0;
    long lastTime;



    //mouse
    public void addMouseMoveEvent(double mouseX, double mouseY) {
        latestX = (int) mouseX;
        latestY = this.height - (int) mouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseDX() {
        return mouseX - lastMouseX;
    }

    public int getMouseDY() {
        return mouseY - lastMouseY;
    }

    public boolean isMouseGrabbed() {
        return this.mouseGrabbed;
    }

    public void setMouseGrabbed(boolean grab) {
        GLFW.glfwSetInputMode(this.handle, 208897, grab ? 212995 : 212993);
        this.mouseGrabbed = grab;
    }

    public void setCursorPosition(int newX, int newY) {
        GLFW.glfwSetCursorPos(this.handle, newX, newY);
    }



    //device
    public void create(boolean compatMode) {


        //get handle
        GLFWErrorCallback.createPrint(System.err).set();
        handle = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (handle == 0L) {
            throw new IllegalStateException("Failed to create Display window");
        }
        GLFW.glfwDefaultWindowHints();

        //set callback
        initCallBacks();

        //set gl context
        MonitorInfo monitorInfo = getMonitorInfo();
        x = (monitorInfo.width() - width) / 2;
        y = (monitorInfo.height() - height) / 2;
        GLFW.glfwSetWindowPos(handle, x, y);
        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(handle);
        if (compatMode) {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        }
        GL.createCapabilities();
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
    }

    private void initCallBacks() {
        GLFW.glfwSetKeyCallback(this.handle, new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == 1) {
                    windowEvent.callEvent(new KeyPressEvent(Keyboard.toLwjglKey(key)));
                }
                if (action == 0) {
                    windowEvent.callEvent(new KeyReleaseEvent(Keyboard.toLwjglKey(key)));
                }
                if (action == 2) {
                    windowEvent.callEvent(new KeyHoldEvent(Keyboard.toLwjglKey(key)));
                }
            }
        });
        GLFW.glfwSetCharCallback(this.handle, new GLFWCharCallback() {
            public void invoke(long window, int codepoint) {
                windowEvent.callEvent(new CharEvent((char) codepoint));
            }
        });
        GLFW.glfwSetWindowSizeCallback(this.handle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int w, int h) {
                latestResized = true;
                latestWidth = w;
                latestHeight = h;
            }
        });
        GLFW.glfwSetWindowFocusCallback(this.handle, new GLFWWindowFocusCallback() {
            public void invoke(long window, boolean focus) {
                focused = focus;
            }
        });
        GLFW.glfwSetWindowIconifyCallback(this.handle, new GLFWWindowIconifyCallback() {
            public void invoke(long window, boolean iconified) {
                visible = iconified;
            }
        });
        GLFW.glfwSetWindowPosCallback(this.handle, new GLFWWindowPosCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                x = i;
                y = i1;
            }
        });
        GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int i, long l) {
                throw new IllegalStateException(String.valueOf(i));
            }
        });

        GLFW.glfwSetCursorPosCallback(this.handle, new GLFWCursorPosCallback() {
            public void invoke(long window, double xPos, double yPos) {
                addMouseMoveEvent(xPos, yPos);
            }
        });
        GLFW.glfwSetMouseButtonCallback(this.handle, new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                if (action != 1) {
                    windowEvent.callEvent(new MouseClickEvent(Window.this, mouseX, mouseY, button));
                }
            }
        });
        GLFW.glfwSetScrollCallback(this.handle, new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                scroll[0] = -v1;
                windowEvent.callEvent(new MouseScrollEvent((int) -v1));
            }
        });
    }

    public EventBus getEventBus() {
        return windowEvent;
    }



    //display
    public MonitorInfo getMonitorInfo() {
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = Objects.requireNonNull(GLFW.glfwGetVideoMode(monitor));
        return new MonitorInfo(
                vidMode.width(), vidMode.height(),
                vidMode.blueBits() + vidMode.greenBits() + vidMode.redBits(),
                vidMode.refreshRate()
        );
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();

        lastMouseX = mouseX;
        lastMouseY = mouseY;
        if (!mouseGrabbed) {
            if (latestX < 0) {
                latestX = 0;
            }

            if (latestY < 0) {
                latestY = 0;
            }

            if (latestX > this.width - 1) {
                latestX = this.width - 1;
            }

            if (latestY > this.height - 1) {
                latestY = this.height - 1;
            }
        }

        mouseX = latestX;
        mouseY = latestY;
        if (latestResized) {
            latestResized = false;
            width = latestWidth;
            height = latestHeight;
        }
    }

    public void setWindowVsyncEnable(boolean vsync) {
        GLFW.glfwSwapInterval(vsync ? 1 : 0);
    }

    public void setWindowTitle(String title) {
        GLFW.glfwSetWindowTitle(handle, title);
        this.title = title;
    }

    public long getWindowHandle() {
        return handle;
    }

    public void setWindowFullscreen(boolean fullscreen) {
        fullScreen = fullscreen;
        if (fullScreen) {
            lastNFullScreenWidth = width;
            lastNFullScreenHeight = height;
            lastNFullScreenX = x;
            lastNFullScreenY = y;
            GLFW.glfwSetWindowMonitor(handle, GLFW.glfwGetPrimaryMonitor(),
                    0, 0,
                    getMonitorInfo().width(), getMonitorInfo().height(), getMonitorInfo().freshRate()
            );

        } else {
            GLFW.glfwSetWindowMonitor(handle, 0,
                    lastNFullScreenX, lastNFullScreenY,
                    lastNFullScreenWidth, lastNFullScreenHeight, 60
            );
        }
    }

    public boolean isWindowFullscreen() {
        return fullScreen;
    }

    public boolean isWindowCloseRequested() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public boolean isWindowActive() {
        return focused;
    }

    public void setWindowIcon(InputStream in) {
        GLFWImage image = GLFWImage.malloc();
        BufferedImage img;
        try {
            img = ImageIO.read(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteBuffer buffer = ImageUtil.getByteFromBufferedImage_RGBA(img);
        image.set(img.getWidth(), img.getHeight(), buffer);
        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);
        GLFW.glfwSetWindowIcon(handle, images);
        images.free();
        image.free();
        BufferAllocation.free(buffer);
    }

    public void setWindowSize(int width, int height) {
        GLFW.glfwSetWindowSize(handle, width, height);
    }

    public void setWindowPos(int x, int y) {
        GLFW.glfwSetWindowSize(handle, x, y);
    }

    public int getWindowWidth() {
        return width;
    }

    public int getWindowHeight() {
        return height;
    }

    public boolean isWindowVisible() {
        return visible;
    }

    public float getAspect() {
        return width / (float) height;
    }

    public void hint(int name, int value) {
        GLFW.glfwWindowHint(name, value);
    }



    //keyboard
    public boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(this.handle, Keyboard.toGlfwKey(key)) == 1;
    }

    public boolean isKeyDoubleClicked(int key, float timeElapse) {
        if (this.isKeyDown(key)) {
            if (!keyDownStatus) {
                keyDownStatus = true;
                if (keyDownCount == 0) {// ????????????????????? 0
                    lastTime = System.currentTimeMillis();// ??????????????????
                }
                keyDownCount++;
            }
        }
        if (!this.isKeyDown(key)) {
            keyDownStatus = false;
        }
        if (keyDownStatus) {
            if (keyDownCount >= 2) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime < timeElapse) {
                    lastTime = currentTime;
                    keyDownCount = 0;
                    return true;//???????????????????????????
                } else {
                    lastTime = System.currentTimeMillis();  // ??????????????????
                    keyDownCount = 1;
                }
            }
        }
        return false;
    }

    public int getMouseFixedY() {
        return -this.getMouseY() + this.getWindowHeight();
    }
}