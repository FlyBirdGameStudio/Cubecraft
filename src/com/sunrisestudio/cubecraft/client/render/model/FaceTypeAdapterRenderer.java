package com.sunrisestudio.cubecraft.client.render.model;

import com.sunrisestudio.cubecraft.client.render.model.object.ModelObject;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

public interface FaceTypeAdapterRenderer {
    void render(World worldAccess, IVertexArrayBuilder builder, ModelObject modelObject,
                double x0, double x1, double y0, double y1, double z0, double z1,
                float u0, float u1, float v0, float v1, int l,
                long worldX, long worldY, long worldZ
    );
}
