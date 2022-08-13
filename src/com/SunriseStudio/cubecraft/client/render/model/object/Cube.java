package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

import org.joml.Matrix4f;
import org.joml.Vector3d;

public record Cube(
        Vector3d v0,Vector3d v1,
        Face top, Face bottom,
        Face left, Face right,
        Face front ,Face back
){
    public void render(Vector3d renderPos, Matrix4f mvp, IVertexArrayBuilder builder, boolean[] shouldRender) {
        double x0=this.v0.x;
        double y0=this.v0.y;
        double z0=this.v0.z;

        double x1=this.v1.x;
        double y1=this.v1.y;
        double z1=this.v1.z;



        if (shouldRender[0]) {
            float u0= (float) this.top.tc0().x;
            float v0= (float) this.top.tc0().y;
            float u1= (float) this.top.tc1().x;
            float v1= (float) this.top.tc1().y;
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x0, y0, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u1, v0);
            builder.vertexUV(x1, y0, z1, u1, v1);
        }
        if (shouldRender[1]) {
            float u0= (float) this.bottom.tc0().x;
            float v0= (float) this.bottom.tc0().y;
            float u1= (float) this.bottom.tc1().x;
            float v1= (float) this.bottom.tc1().y;
            builder.vertexUV(x1, y1, z1, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y1, z1, u0, v1);

        }
        if (shouldRender[2]) {
            float u0= (float) this.left.tc0().x;
            float v0= (float) this.left.tc0().y;
            float u1= (float) this.left.tc1().x;
            float v1= (float) this.left.tc1().y;
            builder.vertexUV(x0, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z0, u1, v1);

        }
        if (shouldRender[3]) {
            Face f=this.right;
            float u0= (float) f.tc0().x;
            float v0= (float) f.tc0().y;
            float u1= (float) f.tc1().x;
            float v1= (float) f.tc1().y;
            builder.vertexUV(x0, y1, z1, u0, v0);
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z1, u1, v1);
            builder.vertexUV(x1, y1, z1, u1, v0);

        }
        if (shouldRender[4]) {
            float u0= (float) this.front.tc0().x;
            float v0= (float) this.front.tc0().y;
            float u1= (float) this.front.tc1().x;
            float v1= (float) this.front.tc1().y;
            builder.vertexUV(x0, y1, z1, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z1, u1, v1);

        }
        if (shouldRender[5]) {
            float u0= (float) this.back.tc0().x;
            float v0= (float) this.back.tc0().y;
            float u1= (float) this.back.tc1().x;
            float v1= (float) this.back.tc1().y;
            builder.vertexUV(x1, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z0, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z1, u0, v0);
        }
    }

    public void renderInner(Vector3d renderPos, Matrix4f mvp, IVertexArrayBuilder builder, boolean[] shouldRender) {
        double x0=new Vector3d(this.v0).mulProject(mvp).x;
        double y0=new Vector3d(this.v0).mulProject(mvp).y;
        double z0=new Vector3d(this.v0).mulProject(mvp).z;

        double x1=new Vector3d(this.v1).mulProject(mvp).x;
        double y1=new Vector3d(this.v1).mulProject(mvp).y;
        double z1=new Vector3d(this.v1).mulProject(mvp).z;

        if (shouldRender[1]) {
            float u0= (float) this.top.tc0().x;
            float v0= (float) this.top.tc0().y;
            float u1= (float) this.top.tc1().x;
            float v1= (float) this.top.tc1().y;
            builder.vertexUV(x0, y1-0.001, z1, u0, v1);
            builder.vertexUV(x0, y1-0.001, z0, u0, v0);
            builder.vertexUV(x1, y1-0.001, z0, u1, v0);
            builder.vertexUV(x1, y1-0.001, z1, u1, v1);
        }
        if (shouldRender[0]) {
            float u0= (float) this.bottom.tc0().x;
            float v0= (float) this.bottom.tc0().y;
            float u1= (float) this.bottom.tc1().x;
            float v1= (float) this.bottom.tc1().y;
            builder.vertexUV(x1, y0+0.001, z1, u1, v1);
            builder.vertexUV(x1, y0+0.001, z0, u1, v0);
            builder.vertexUV(x0, y0+0.001, z0, u0, v0);
            builder.vertexUV(x0, y0+0.001, z1, u0, v1);
        }
        if (shouldRender[3]) {
            float u0= (float) this.left.tc0().x;
            float v0= (float) this.left.tc0().y;
            float u1= (float) this.left.tc1().x;
            float v1= (float) this.left.tc1().y;
            builder.vertexUV(x0, y1, z1-0.001, u1, v0);
            builder.vertexUV(x1, y1, z1-0.001, u0, v0);
            builder.vertexUV(x1, y0, z1-0.001, u0, v1);
            builder.vertexUV(x0, y0, z1-0.001, u1, v1);
        }
        if (shouldRender[2]) {
            float u0= (float) this.right.tc0().x;
            float v0= (float) this.right.tc0().y;
            float u1= (float) this.right.tc1().x;
            float v1= (float) this.right.tc1().y;
            builder.vertexUV(x0, y1, z0+0.001, u0, v0);
            builder.vertexUV(x0, y0, z0+0.001, u0, v1);
            builder.vertexUV(x1, y0, z0+0.001, u1, v1);
            builder.vertexUV(x1, y1, z0+0.001, u1, v0);
            return;
        }
        if (shouldRender[4]) {
            float u0= (float) this.front.tc0().x;
            float v0= (float) this.front.tc0().y;
            float u1= (float) this.front.tc1().x;
            float v1= (float) this.front.tc1().y;
            builder.vertexUV(x1-0.001, y1, z1, u1, v0);
            builder.vertexUV(x1-0.001, y1, z0, u0, v0);
            builder.vertexUV(x1-0.001, y0, z0, u0, v1);
            builder.vertexUV(x1-0.001, y0, z1, u1, v1);
            return;
        }
        if (shouldRender[5]) {
            float u0= (float) this.back.tc0().x;
            float v0= (float) this.back.tc0().y;
            float u1= (float) this.back.tc1().x;
            float v1= (float) this.back.tc1().y;
            builder.vertexUV(x0+0.001, y0, z1, u0, v1);
            builder.vertexUV(x0+0.001, y0, z0, u1, v1);
            builder.vertexUV(x0+0.001, y1, z0, u1, v0);
            builder.vertexUV(x0+0.001, y1, z1, u0, v0);
        }
    }

    public Face getFace(int i) {
        return switch (i){
            case 0->top;
            case 1->bottom;
            case 2->left;
            case 3->right;
            case 4->front;
            case 5->back;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}