package com.flybirdstudio.cubecraft.client.render.model;

import com.flybirdstudio.cubecraft.client.render.model.object.ModelObject;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public interface FaceTypeAdapterRenderer {
    void render(IWorld worldAccess, VertexArrayBuilder builder, ModelObject modelObject,
                double x0, double x1, double y0, double y1, double z0, double z1,
                float u0, float u1, float v0, float v1, int l,
                long worldX, long worldY, long worldZ
    );
}
