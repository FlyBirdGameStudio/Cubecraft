package com.SunriseStudio.cubecraft.util.grass3D.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class ShapeRenderer {
    private static Tesselator t=Tesselator.instance;
    public static void drawRectUV(double x0,double x1,double y0,double y1,double z0,double z1,double u0,double u1,double v0,double v1){
        t.begin();
        t.vertexUV(x1, y0, z0, (float)u1, (float)v0);
        t.vertexUV(x0, y0, z0, (float)u0, (float)v0);
        t.vertexUV(x0, y1, z0, (float)u0, (float)v1);
        t.vertexUV(x1, y1, z0, (float)u1, (float)v1);
        t.end();
    }
    public static void drawRect(double x0,double x1,double y0,double y1,double z0,double z1){
        t.begin();
        t.vertex((float)x0, (float)y1, (float)z1);
        t.vertex((float)x1, (float)y1, (float)z0);
        t.vertex((float)x1, (float)y0, (float)z0);
        t.vertex((float)x0, (float)y0, (float)z1);
        t.end();
    }
    public static void setColor(int r,int g,int b,int a){
        GL11.glColor4ub((byte) r,(byte)g,(byte)b,(byte)a);
    }
    public static void setColor(int color) {
        int r = (color >> 16 & 0xFF) ;
        int g = (color >> 8 & 0xFF) ;
        int b = (color & 0xFF) ;
       setColor(r,g,b,255);
    }

    public static void begin() {
        t.begin();
    }
    public static void end(){
        t.end();
    }

    public static class Tesselator {
        private static final int MAX_MEMORY_USE = 0x400000;
        private static final int MAX_FLOATS = 524288;
        private FloatBuffer buffer = BufferUtils.createFloatBuffer(524288);
        private float[] array = new float[524288];
        private int vertices = 0;
        private float u;
        private float v;
        private float r;
        private float g;
        private float b;
        private boolean hasColor = false;
        private boolean hasTexture = false;
        private int len = 3;
        private int p = 0;
        private boolean noColor = false;
        public static Tesselator instance = new Tesselator();

        private Tesselator() {
        }

        public void end() {
            if (this.vertices > 0) {
                this.buffer.clear();
                this.buffer.put(this.array, 0, this.p);
                this.buffer.flip();
                if (this.hasTexture && this.hasColor) {
                    GL11.glInterleavedArrays(10794, 0, this.buffer);
                } else if (this.hasTexture) {
                    GL11.glInterleavedArrays(10791, 0, this.buffer);
                } else if (this.hasColor) {
                    GL11.glInterleavedArrays(10788, 0, this.buffer);
                } else {
                    GL11.glInterleavedArrays(10785, 0, this.buffer);
                }
                GL11.glEnableClientState(32884);
                if (this.hasTexture) {
                    GL11.glEnableClientState(32888);
                }
                if (this.hasColor) {
                    GL11.glEnableClientState(32886);
                }
                GL11.glDrawArrays(7, 0, this.vertices);
                GL11.glDisableClientState(32884);
                if (this.hasTexture) {
                    GL11.glDisableClientState(32888);
                }
                if (this.hasColor) {
                    GL11.glDisableClientState(32886);
                }
            }
            this.clear();
        }

        private void clear() {
            this.vertices = 0;
            this.buffer.clear();
            this.p = 0;
        }

        public void begin() {
            this.clear();
            this.hasColor = false;
            this.hasTexture = false;
            this.noColor = false;
        }

        public void tex(float u, float v) {
            if (!this.hasTexture) {
                this.len += 2;
            }
            this.hasTexture = true;
            this.u = u;
            this.v = v;
        }

        public void color(int r, int g, int b) {
            this.color((byte)r, (byte)g, (byte)b);
        }

        public void color(byte r, byte g, byte b) {
            if (this.noColor) {
                return;
            }
            if (!this.hasColor) {
                this.len += 3;
            }
            this.hasColor = true;
            this.r = (float)(r & 0xFF) / 255.0f;
            this.g = (float)(g & 0xFF) / 255.0f;
            this.b = (float)(b & 0xFF) / 255.0f;
        }

        public void vertexUV(double x, double y, double z, float u, float v) {
            this.tex(u, v);
            this.vertex((float) x, (float) y, (float) z);
        }

        public void vertex(float x, float y, float z) {
            if (this.hasTexture) {
                this.array[this.p++] = this.u;
                this.array[this.p++] = this.v;
            }
            if (this.hasColor) {
                this.array[this.p++] = this.r;
                this.array[this.p++] = this.g;
                this.array[this.p++] = this.b;
            }
            this.array[this.p++] = x;
            this.array[this.p++] = y;
            this.array[this.p++] = z;
            ++this.vertices;
            if (this.vertices % 4 == 0 && this.p >= 524288 - this.len * 4) {
                this.end();
            }
        }

        public void color(int c) {
            int r = c >> 16 & 0xFF;
            int g = c >> 8 & 0xFF;
            int b = c & 0xFF;
            this.color(r, g, b);
        }

        public void noColor() {
            this.noColor = true;
        }
    }


    public static void begin2(){
        int array=0;

        GL30.glBindVertexArray(array);
        GL20.glEnableVertexAttribArray(array);
    }

















}
