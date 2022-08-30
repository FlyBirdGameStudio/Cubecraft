package com.flybirdstudio.cubecraft.client.render.model.character;

import org.lwjgl.opengl.GL11;

public class Polygon {
    public Vertex[] vertices;
    public int vertexCount = 0;

    public Polygon(Vertex[] vertices) {
        this.vertices = vertices;
        this.vertexCount = vertices.length;
    }

    public Polygon(Vertex[] vertices, int u0, int v0, int u1, int v1) {
        this(vertices);
        vertices[0] = vertices[0].remap(u1, v0);
        vertices[1] = vertices[1].remap(u0, v0);
        vertices[2] = vertices[2].remap(u0, v1);
        vertices[3] = vertices[3].remap(u1, v1);
    }

    public void render() {
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        for (int i = 3; i >= 0; --i) {
            Vertex v = this.vertices[i];
            GL11.glTexCoord2f(v.u / 63.999f, v.v / 31.999f);
            GL11.glVertex3f(v.pos.x, v.pos.y, v.pos.z);
        }
    }
}
