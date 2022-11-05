package io.flybird.starfish3d.platform;

import org.lwjgl.glfw.*;


public class Callbacks {
    public static GLFWErrorCallback errorCallback;
    public static GLFWMonitorCallback monitorCallback;
    
    //window
    public static GLFWWindowPosCallback windowPosCallback;
    public static GLFWWindowFocusCallback windowFocusCallback;
    public static GLFWWindowIconifyCallback windowIconifyCallback;
    public static GLFWFramebufferSizeCallback framebufferSizeCallback;
    public static GLFWWindowSizeCallback windowSizeCallback;
    public static GLFWWindowRefreshCallback windowRefreshCallback;
    
    //key
    public static GLFWCharCallback charCallback;
    public static GLFWKeyCallback keyCallback;
    
    //mouse
    public static GLFWCursorPosCallback cursorPosCallback;
    public static GLFWMouseButtonCallback mouseButtonCallback;
    public static GLFWScrollCallback scrollCallback;

    public static void setCallback(long handle){
        GLFW.glfwSetErrorCallback(errorCallback);
        GLFW.glfwSetMonitorCallback(monitorCallback);
        
        GLFW.glfwSetWindowPosCallback(handle,windowPosCallback);
        GLFW.glfwSetWindowFocusCallback(handle,windowFocusCallback);
        GLFW.glfwSetWindowIconifyCallback(handle,windowIconifyCallback);
        GLFW.glfwSetFramebufferSizeCallback(handle,framebufferSizeCallback);
        GLFW.glfwSetWindowSizeCallback(handle,windowSizeCallback);
        GLFW.glfwSetWindowRefreshCallback(handle,windowRefreshCallback);
        
        GLFW.glfwSetCharCallback(handle,charCallback);
        GLFW.glfwSetKeyCallback(handle,keyCallback);
        
        GLFW.glfwSetCursorPosCallback(handle,cursorPosCallback);
        GLFW.glfwSetMouseButtonCallback(handle,mouseButtonCallback);
        GLFW.glfwSetScrollCallback(handle,scrollCallback);
    }

    public static void releaseCallback() {
        keyCallback.free();
        charCallback.free();
        cursorPosCallback.free();
        mouseButtonCallback.free();
        windowFocusCallback.free();
        windowIconifyCallback.free();
        windowSizeCallback.free();
        windowPosCallback.free();
        windowRefreshCallback.free();
        framebufferSizeCallback.free();
        errorCallback.free();
        monitorCallback.free();
        scrollCallback.free();
    }
}
