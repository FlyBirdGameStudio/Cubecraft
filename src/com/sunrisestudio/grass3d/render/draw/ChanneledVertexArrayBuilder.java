package com.sunrisestudio.grass3d.render.draw;

import com.sunrisestudio.util.container.buffer.BufferBuilder;
import com.sunrisestudio.util.container.ArrayUtil;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

/**
 * this builder grouped any type of attribute in to channel.
 * it is more efficient when using{@link PointedVertexArrayUploader}.
 * it is still compatible with any type of {@link IVertexArrayUploader},but it will not working well.
 */
public class ChanneledVertexArrayBuilder implements IVertexArrayBuilder {

    //data
    public int vertexCount = 0;
    double[] color;
    double[] normal;
    double[] tc;
    double[] vertex;
    double[] raw;

    //texture
    private float u;
    private float v;
    private float layer;

    //color
    private float r = 1.0f;
    private float g = 1.0f;
    private float b = 1.0f;
    private float a = 1.0f;

    //normal
    private float n = 1.0f;
    private float f = 1.0f;
    private float l = 1.0f;

    public ChanneledVertexArrayBuilder(int size) {
        color = new double[size*4];
        normal = new double[size*3];
        tc = new double[size*3];
        vertex = new double[size*3];
    }

    public void clear() {
        this.vertexCount = 0;
    }

    @Override
    public void end() {
        this.raw=new double[vertexCount*13];
        for (int i=0;i<vertexCount;i++){
            raw[i*13]=tc[i*3];
            raw[i*13+1]=tc[i*3+1];
            raw[i*13+2]=tc[i*3+2];

            raw[i*13+3]=color[i*4];
            raw[i*13+4]=color[i*4+1];
            raw[i*13+5]=color[i*4+2];
            raw[i*13+6]=color[i*4+3];

            raw[i*13+7]=normal[i*3];
            raw[i*13+8]=normal[i*3+1];
            raw[i*13+9]=normal[i*3+2];

            raw[i*13+10]=vertex[i*3];
            raw[i*13+11]=vertex[i*3+1];
            raw[i*13+12]=vertex[i*3+2];
        }
    }

    public void begin() {
        this.clear();
    }

    //color
    public void color(int c) {
        byte r = (byte) (c >> 16);
        byte g = (byte) (c >> 8);
        byte b = (byte) c;
        this.colorB(r, g, b, (byte) 255);
    }

    public void colorB(byte r, byte g, byte b) {
        float r2 = (r & 0xFF) / 255.0f;
        float g2 = (g & 0xFF) / 255.0f;
        float b2 = (b & 0xFF) / 255.0f;
        this.color(r2, g2, b2, 1.0f);
    }

    public void colorB(byte r, byte g, byte b, byte a) {
        float r2 = (r & 0xFF) / 255.0f;
        float g2 = (g & 0xFF) / 255.0f;
        float b2 = (b & 0xFF) / 255.0f;
        float a2 = (a & 0xFF) / 255.0f;
        this.color(r2, g2, b2, a2);
    }

    @Override
    public final void color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    //texture

    @Override
    public void vertexUV(double x, double y, double z, float u, float v) {
        this.vertexUV(x, y, z, u, v, 0.0f);
    }

    @Override
    public void vertexUV(double x, double y, double z, float u, float v, float layer) {
        this.tex(u, v, layer);
        this.vertex(x, y, z);
    }

    @Override
    public void tex(float u, float v, float layer) {
        this.u = u;
        this.v = v;
        this.layer = layer;
    }

    public void vertex(double x, double y, double z) {
        color[this.vertexCount * 4] = r;
        color[this.vertexCount * 4 + 1] = g;
        color[this.vertexCount * 4 + 2] = b;
        color[this.vertexCount * 4 + 3] = a;

        normal[this.vertexCount * 3] = n;
        normal[this.vertexCount * 3 + 1] = f;
        normal[this.vertexCount * 3 + 2] = l;

        tc[this.vertexCount * 3] = u;
        tc[this.vertexCount * 3 + 1] = v;
        tc[this.vertexCount * 3 + 2] = layer;

        vertex[this.vertexCount * 3] = (float) x;
        vertex[this.vertexCount * 3 + 1] = (float) y;
        vertex[this.vertexCount * 3 + 2] = (float) z;
        ++this.vertexCount;
    }

    @Override
    public void normal(float n, float f, float l) {
        this.n = n;
        this.f = f;
        this.l = l;
    }

    public int getVertexCount() {
        return vertexCount;
    }




    @Override
    public DoubleBuffer getRawBuffer() {
        return BufferBuilder.getD(this.raw);
    }

    @Override
    public DoubleBuffer getVertexBuffer() {
        return BufferBuilder.getD(ArrayUtil.copySub(0,vertexCount*3,vertex));
    }

    @Override
    public DoubleBuffer getNormalBuffer() {
        return BufferBuilder.getD(ArrayUtil.copySub(0,vertexCount*3,normal));
    }

    @Override
    public DoubleBuffer getColorBuffer() {
        return BufferBuilder.getD(ArrayUtil.copySub(0,vertexCount*4,color));
    }

    @Override
    public DoubleBuffer getTexCoordBuffer() {
        return BufferBuilder.getD(ArrayUtil.copySub(0,vertexCount*3,tc));
    }
}
