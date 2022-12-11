package io.flybird.starfish3d.render;

import io.flybird.util.container.BufferUtil;
import org.joml.Matrix4d;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class GLUtil {
    /**
     * setup fog
     * @param distance distance of fog
     * @param color color,4 floats
     */
    public static void setupFog(int distance, float[] color){
        GLUtil.assertRenderThread();
        GL11.glEnable(GL11.GL_FOG);
        FloatBuffer buffer= BufferUtil.from(color);
        GL11.glFogfv(GL11.GL_FOG_COLOR, color);
        BufferUtil.free(buffer);
        GL11.glFogf(GL11.GL_FOG_DENSITY,(0.6f/((float)distance)));
        GL11.glHint(GL11.GL_FOG_HINT,GL11.GL_NICEST);
        GL11.glFogi(GL11.GL_FOG_MODE,GL11.GL_EXP);
    }

    /**
     * check if the caller of current thread of openGL context exist
     * @throws IllegalStateException when it is not on openGL thread
     */
    public static void assertRenderThread() throws IllegalStateException{
        try{
            GL11.glColor4f(1,1,1,1);
        }catch (Exception e){
            throw new IllegalStateException("you are not at the render context thread!");
        }
    }

    /**
     * create a perspective camera
     * @param fov field of view
     * @param width screen width
     * @param height screen height
     */
    public static void setupPerspectiveCamera(float fov,int width,int height){
        GLUtil.assertRenderThread();
        GL11.glMatrixMode(5889);
        loadIdentity();
        createPerspectiveMatrix(fov, (float) width/height,0.05,131072);
        GL11.glMatrixMode(5888);
        loadIdentity();
    }

    /**
     * create an orthogonal camera
     * @param x screen start x
     * @param y screen start y
     * @param displayWidth screen width
     * @param displayHeight screen height
     * @param w matrix width
     * @param h matrix height
     */
    public static void setupOrthogonalCamera(int x, int y, int displayWidth, int displayHeight, int w, int h){
        GLUtil.assertRenderThread();
        GL11.glViewport(x,y,displayWidth,displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        loadIdentity();
        GL11.glOrtho(x, w,h, y, -100, 100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        loadIdentity();
    }

    /**
     * create a perspective camera
     * @param fov field of view
     * @param aspect screen width/screen height
     * @param zn z near
     * @param zf z far
     */
    public static void createPerspectiveMatrix(double fov, double aspect, double zn, double zf){
        GLUtil.assertRenderThread();
        Matrix4d mat=new Matrix4d();
        loadIdentity();
        mat.perspective(fov,aspect,zn,zf);
        DoubleBuffer matrix = BufferUtils.createDoubleBuffer(16);
        GL11.glMultMatrixd(mat.get(matrix));
    }

    /**
     * enable blend to openGL
     */
    public static void enableBlend() {
        GLUtil.assertRenderThread();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER,0.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * disable blend to openGL
     */
    public static void disableBlend() {
        GLUtil.assertRenderThread();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * enable multi sample to openGL
     */
    public static void enableMultiSample(){
        GLUtil.assertRenderThread();
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    }

    /**
     * disable multi sample to openGL
     */
    public static void disableMultiSample(){
        GLUtil.assertRenderThread();
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        disableBlend();
    }


    public static void loadIdentity(){
        GL11.glLoadMatrixd(new Matrix4d().identity().get(new double[16]));
    }

    public static void enableDepthTest() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    public static void disableDepthTest() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void allEnableClientState(){
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public static void allDisableClientState(){
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public static void checkGLError(String status) {
        int errorStatus = GL11.glGetError();
        if (errorStatus != 0) {
            throw new RuntimeException(errorStatus + ":" + status);
        }
    }
}
