package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

import java.util.Objects;

public class Face {
    final float u0, u1, v0, v1;
    final String texture, culling, renderType, id;

    public Face(double u0, double u1, double v0, double v1, String texture, String culling, String renderType, String id) {
        this.u0 = (float) u0;
        this.u1 = (float) u1;
        this.v0 = (float) v0;
        this.v1 = (float) v1;
        this.texture = texture;
        this.culling = culling;
        this.renderType = renderType;
        this.id = id;
    }

    public void render(IWorldAccess worldAccess, IVertexArrayBuilder builder, ModelObject modelObject, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, String renderType) {
        double x0 = renderX + modelObject.x0;
        double x1 = renderX + modelObject.x1;
        double z0 = renderZ + modelObject.z0;
        double z1 = renderZ + modelObject.z1;
        double y0 = renderY + modelObject.y0;
        double y1 = renderY + modelObject.y1;
        int l = Registry.getTextureManager().get2DArrayTexture("cubecraft:terrain").getLayer(this.texture);
        if (Objects.equals(renderType, this.renderType)) {
            Registry.getFaceTypeAdapterRendererMap().get(this.id).render(
                    worldAccess, builder, modelObject,
                    x0,x1,y0,y1,z0,z1,
                    u0,u1,v0,v1,l,
                    worldX, worldY, worldZ
            );
        }
    }
}
