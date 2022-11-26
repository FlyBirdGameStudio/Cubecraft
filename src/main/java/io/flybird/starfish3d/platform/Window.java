package io.flybird.starfish3d.platform;

/*
public class Window {
    public static void initGLFW(){
        GLFW.glfwInit();
    }

    public static void distroyGLFW(){
        GLFW.glfwTerminate();
    }

    public EventBus windowEvent=new EventBus();
    private long handle;

    private boolean visible;
    private boolean focused;
    private boolean dirty;

    private int fboWidth, fboHeight;
    private int x, y;
    private int width=1280, height=720;
    private boolean latestResized;
    private String title="Grass3D window";
    private int latestWidth,latestHeight;
    private boolean resized;
    private boolean fullScreen;
    private boolean resizeable;
    private boolean focus;


    private int lastNFullScreenWidth,lastNFullScreenHeight;
    private int lastNFullScreenX,lastNFullScreenY;

    public void create(){
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
    }


    private void getDisplaySize(){
        IntBuffer fbw = BufferUtils.createIntBuffer(1);
        IntBuffer fbh = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(handle, fbw, fbh);
        fboWidth = fbw.get(0);
        fboHeight = fbh.get(0);
    }

    private MonitorInfo getMonitorInfo(){
        long monitor = GLFW.glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(monitor);
        return new MonitorInfo(
                vidmode.width(), vidmode.height(),
                vidmode.blueBits()+vidmode.greenBits()+ vidmode.redBits(),
                vidmode.refreshRate()
        );
    }

    private void initCallBacks() {
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

    public void update() {
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

    public void setVsyncEnable(boolean vsync){
        GLFW.glfwSwapInterval(vsync?1:0);
    }

    public void setSample(int FXAALevel){
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,FXAALevel);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(handle,title);
    }

    public long getHandle() {
        return handle;
    }

    public void destroy() {
        Callbacks.releaseCallback();
        GLFW.glfwDestroyWindow(handle);
    }

    public void setFullscreen(boolean fullscreen) {
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

    public boolean isFullscreen() {
        return fullScreen;
    }

    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void setFXAA(int fxaa) {
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,fxaa);
    }

    public void setResizable(boolean b) {
        resizeable=b;
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,b?1:0);
    }

    public void sync(int i) {
        Sync.sync(i);
    }

    public boolean isActive() {
        return focused;
    }

    public void setIcon(InputStream in)  {
        GLFWImage image = GLFWImage.malloc();
        BufferedImage img;
        try {
            img= ImageIO.read(in);
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

    public void setSize(int width,int height){
        GLFW.glfwSetWindowSize(handle,width,height);
    }

    public void setPos(int x,int y){
        GLFW.glfwSetWindowSize(handle,x,y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear(){
        GL11.glClear(16640);
    }
}


 */