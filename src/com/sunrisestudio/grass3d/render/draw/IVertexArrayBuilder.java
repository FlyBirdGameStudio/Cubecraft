package com.sunrisestudio.grass3d.render.draw;

import org.joml.Vector2f;
import org.joml.Vector3d;

import java.nio.FloatBuffer;

public interface IVertexArrayBuilder {
    void end();

    void begin();

    //color
    void color(int c);

    void colorB(byte r, byte g, byte b);

    void colorB(byte r, byte g, byte b, byte a);

    void color(float r, float g, float b, float a);

    //texture
    void vertexUV(double x, double y, double z, float u, float v);

    void vertexUV(double x, double y, double z, float u, float v, float layer);

    void tex(float u, float v, float layer);

    void vertex(double x, double y, double z);

    void normal(float n, float f, float l);

    int getVertexCount();

    default void vertexUV(Vector3d xyz, Vector2f uv, int layer){
        vertexUV(xyz.x,xyz.y,xyz.z,uv.x,uv.y,layer);
    }

    FloatBuffer getRawBuffer();

    FloatBuffer getVertexBuffer();

    FloatBuffer getNormalBuffer();

    FloatBuffer getColorBuffer();

    FloatBuffer getTexCoordBuffer();
}
