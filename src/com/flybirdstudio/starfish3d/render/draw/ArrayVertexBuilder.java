package com.flybirdstudio.starfish3d.render.draw;

/*
public class ArrayVertexBuilder implements IVertexArrayBuilder {
    //data
    private final double[] array;
    public int vertexCount = 0;

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

    private int p = 0;

    public ArrayVertexBuilder(int size) {
        this.array = new double[size];
    }

    public void clear() {
        this.vertexCount = 0;
        this.p = 0;
    }

    @Override
    public void end() {
        //nothing.....
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
        float r2 =  (r & 0xFF) / 255.0f;
        float g2 =  (g & 0xFF) / 255.0f;
        float b2 =  (b & 0xFF) / 255.0f;
        float a2 =  (a & 0xFF) / 255.0f;
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
        //"t4f"
        this.array[this.p++] = this.u;
        this.array[this.p++] = this.v;
        this.array[this.p++] = this.layer;
        this.array[this.p++] = 1f;

        //"c4f"
        this.array[this.p++] = this.r;
        this.array[this.p++] = this.g;
        this.array[this.p++] = this.b;
        this.array[this.p++] = this.a;

        //"n3f"
        this.array[this.p++] = this.n;
        this.array[this.p++] = this.f;
        this.array[this.p++] = this.l;

        //"v4f"
        this.array[this.p++] = x;
        this.array[this.p++] = y;
        this.array[this.p++] = z;
        this.array[this.p++] = 1.0f;
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
        return BufferBuilder.getD(array);
    }

    @Override
    public DoubleBuffer getVertexBuffer() {
        return null;
    }

    @Override
    public DoubleBuffer getNormalBuffer() {
        return null;
    }

    @Override
    public DoubleBuffer getColorBuffer() {
        return null;
    }

    @Override
    public DoubleBuffer getTexCoordBuffer() {
        return null;
    }


}


 */
