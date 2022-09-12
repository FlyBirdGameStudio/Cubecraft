package com.flybirdstudio.starfish3d.render.draw;

import com.flybirdstudio.util.ColorUtil;
import com.flybirdstudio.util.container.ArrayUtil;
import org.joml.Vector2f;
import org.joml.Vector3d;

import java.nio.ByteOrder;

/**
 * this builder grouped any type of attribute in to channel.
 * it is more efficient when using{@link VertexPointerUploader}.
 * it is still compatible with any type of {@link IVertexArrayUploader},but it will not working well.
 */
public class VertexArrayBuilder {

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

    public VertexArrayBuilder(int size) {
        color = new double[size * 4];
        normal = new double[size * 3];
        tc = new double[size * 3];
        vertex = new double[size * 3];
    }

    public void clear() {
        this.vertexCount = 0;
    }

    public void end() {
        this.raw = new double[vertexCount * 13];
        for (int i = 0; i < vertexCount; i++) {
            raw[i * 13] = tc[i * 3];
            raw[i * 13 + 1] = tc[i * 3 + 1];
            raw[i * 13 + 2] = tc[i * 3 + 2];

            raw[i * 13 + 3] = color[i * 4];
            raw[i * 13 + 4] = color[i * 4 + 1];
            raw[i * 13 + 5] = color[i * 4 + 2];
            raw[i * 13 + 6] = color[i * 4 + 3];

            raw[i * 13 + 7] = normal[i * 3];
            raw[i * 13 + 8] = normal[i * 3 + 1];
            raw[i * 13 + 9] = normal[i * 3 + 2];

            raw[i * 13 + 10] = vertex[i * 3];
            raw[i * 13 + 11] = vertex[i * 3 + 1];
            raw[i * 13 + 12] = vertex[i * 3 + 2];
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

    public final void color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    //texture

    public void vertexUV(double x, double y, double z, float u, float v) {
        this.vertexUV(x, y, z, u, v, 0.0f);
    }

    public void vertexUV(double x, double y, double z, float u, float v, float layer) {
        this.tex(u, v, layer);
        this.vertex(x, y, z);
    }

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

    public void normal(float n, float f, float l) {
        this.n = n;
        this.f = f;
        this.l = l;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public double[] getRawArray() {
        return ArrayUtil.copySub(0, vertexCount * 13, this.raw);
    }

    public double[] getVertexArray() {
        return ArrayUtil.copySub(0, vertexCount * 3, vertex);
    }

    public double[] getNormalArray() {
        return ArrayUtil.copySub(0, vertexCount * 3, normal);
    }

    public double[] getColorArray() {
        return ArrayUtil.copySub(0, vertexCount * 4, color);
    }

    public double[] getTexCoordArray() {
        return ArrayUtil.copySub(0, vertexCount * 3, tc);
    }

    public void vertexUV(Vector3d xyz, Vector2f uv, int layer) {
        vertexUV(xyz.x, xyz.y, xyz.z, uv.x, uv.y, layer);
    }

    public void multColor(float red, float green, float blue) {
        int j = ColorUtil.float3toInt1(this.r,this.g,this.b);
        System.out.println(j);
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            int k = (int) ((float) (j & 255) * red);
            int l = (int) ((float) (j >> 8 & 255) * green);
            int i1 = (int) ((float) (j >> 16 & 255) * blue);
            j = j & -16777216;
            j = j | i1 << 16 | l << 8 | k;
        } else {
            int j1 = (int) ((float) (j >> 24 & 255) * red);
            int k1 = (int) ((float) (j >> 16 & 255) * green);
            int l1 = (int) ((float) (j >> 8 & 255) * blue);
            j = j & 255;
            j = j | j1 << 24 | k1 << 16 | l1 << 8;
        }
        this.r = ColorUtil.int1ToByte3(j)[0];
        this.g = ColorUtil.int1ToByte3(j)[0];
        this.b = ColorUtil.int1ToByte3(j)[0];
    }

    public void multColor(int col){
        multColor(ColorUtil.int1ToFloat3(col)[0],ColorUtil.int1ToFloat3(col)[1],ColorUtil.int1ToFloat3(col)[2]);
    }

    public void multColorB(byte r, byte g, byte b) {
        float r2 = (r & 0xFF) / 255.0f;
        float g2 = (g & 0xFF) / 255.0f;
        float b2 = (b & 0xFF) / 255.0f;

        this.r*=r2;
        this.g*=g2;
        this.b*=b2;
    }
}
