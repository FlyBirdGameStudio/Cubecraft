package com.SunriseStudio.cubecraft.resources.model;

import com.SunriseStudio.cubecraft.util.grass3D.render.draw.IVertexArrayBuilder;
import com.SunriseStudio.cubecraft.resources.model.block.FaceCullingMethod;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import org.joml.Vector2f;
import org.joml.Vector3d;

public record RotatedCube(
        Vector3d from, Vector3d to,

        Vector2f uv_from_f0, Vector2f uv_to_f0, FaceCullingMethod cull_f0, String tex_f0,

        Vector2f uv_from_f1, Vector2f uv_to_f1, FaceCullingMethod cull_f1, String tex_f1,

        Vector2f uv_from_f2, Vector2f uv_to_f2, FaceCullingMethod cull_f2, String tex_f2,

        Vector2f uv_from_f3, Vector2f uv_to_f3, FaceCullingMethod cull_f3, String tex_f3,

        Vector2f uv_from_f4, Vector2f uv_to_f4, FaceCullingMethod cull_f4, String tex_f4,

        Vector2f uv_from_f5, Vector2f uv_to_f5, FaceCullingMethod cull_f5, String tex_f5,

        String id
    ) {

    public void render(IDimensionAccess dimension, IVertexArrayBuilder vertexBuilder, Vector3d world, Vector3d render){
        double x0 = render.x+from.x;
        double x1 = render.x+to.x;
        double y0 = render.y+from.y;
        double y1 = render.y+to.y;
        double z0 = render.z+from.z;
        double z1 = render.z+to.z;
        {
            float u0 = uv_from_f0.x;
            float u1 = uv_to_f0.x;
            float v0 = uv_from_f0.x;
            float v1 = uv_to_f0.x;
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
        }
        {
            float u0 = uv_from_f1.x;
            float u1 = uv_to_f1.x;
            float v0 = uv_from_f1.x;
            float v1 = uv_to_f1.x;
            vertexBuilder.vertexUV(x1, y1, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y1, z1, u0, v1);
        }
        {
            float u0 = uv_from_f2.x;
            float u1 = uv_to_f2.x;
            float v0 = uv_from_f2.x;
            float v1 = uv_to_f2.x;
            vertexBuilder.vertexUV(x0, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x1, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z0, u1, v1);
        }
        {
            float u0 = uv_from_f3.x;
            float u1 = uv_to_f3.x;
            float v0 = uv_from_f3.x;
            float v1 = uv_to_f3.x;
            vertexBuilder.vertexUV(x0, y1, z1, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z1, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z1, u1, v0);
        }
        {
            float u0 = uv_from_f4.x;
            float u1 = uv_to_f4.x;
            float v0 = uv_from_f4.x;
            float v1 = uv_to_f4.x;
            vertexBuilder.vertexUV(x0, y1, z1, u1, v0);
            vertexBuilder.vertexUV(x0, y1, z0, u0, v0);
            vertexBuilder.vertexUV(x0, y0, z0, u0, v1);
            vertexBuilder.vertexUV(x0, y0, z1, u1, v1);
        }
        {
            float u0 = uv_from_f5.x;
            float u1 = uv_to_f5.x;
            float v0 = uv_from_f5.x;
            float v1 = uv_to_f5.x;
            vertexBuilder.vertexUV(x1, y0, z1, u0, v1);
            vertexBuilder.vertexUV(x1, y0, z0, u1, v1);
            vertexBuilder.vertexUV(x1, y1, z0, u1, v0);
            vertexBuilder.vertexUV(x1, y1, z1, u0, v0);
        }
    }
}
