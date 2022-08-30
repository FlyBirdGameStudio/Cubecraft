package com.flybirdstudio.cubecraft.client.render.model.object;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public class Cross extends ModelObject{
    public Cross(double x0, double y0, double z0, double x1, double y1, double z1, Face[] faces, String id) {
        super(x0, y0, z0, x1, y1, z1, faces, id);
    }

    @Override
    public void render(IWorld worldAccess, VertexArrayBuilder builder, String renderType, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ) {

    }
}
