package com.sunrisestudio.cubecraft.resources.model;

import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import org.joml.Vector2d;
import org.joml.Vector3d;

public record Surface(Vector2d uv_from,Vector2d uv_to,String tex,String culling) {
    public boolean shouldRender(long x, long y, long z, IWorldAccess world){

        return switch (this.culling) {
            case ("solid") -> !world.getBlockAccess().getBlock(x,y,z).getMaterial().isSolid();
            case ("equal") -> !(world.getBlockAccess().getBlock(x,y,z).getMaterial().getId()==world.getBlockAccess().getBlock(x,y,z).getMaterial().getId());
            default -> true;
        };
    }

    public void render(IVertexArrayBuilder vertexBuilder, IWorldAccess dimension, Vector3d r0, Vector3d r1, Vector3d wp, int face) {
        double x0=r0.x;
        double x1=r1.x;
        double y0=r0.y;
        double y1=r1.y;
        double z0=r0.z;
        double z1=r1.z;

        float u0= (float) uv_from.x;
        float u1= (float) uv_to.x;
        float v0= (float) uv_from.y;
        float v1= (float) uv_to.y;

        if (face == 0) {
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
            return;
        }
        if (face == 1) {
            vertexBuilder.vertexUV(x1, y1, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y1, z1, u0, v1);
            return;
        }
        if (face == 2) {
            vertexBuilder.vertexUV(x0, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u1, v1);
            return;
        }
        if (face == 3) {
            vertexBuilder.vertexUV(x0, y1, z1, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z1, u1, v0);
            return;
        }
        if (face == 4) {
            vertexBuilder.vertexUV(x0, y1, z1, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z1, u1, v1);
            return;
        }
        if (face == 5) {
            vertexBuilder.vertexUV(x1, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z1, u0, v0);
        }
    }
}
