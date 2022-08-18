package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

public abstract class ModelObject {
    final double x0,y0,z0,x1,y1,z1;
    final Face[] faces;
    final String id;

    public ModelObject(double x0, double y0, double z0, double x1, double y1, double z1, Face[] faces, String id) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.faces = faces;
        this.id = id;
    }

    public void render(
            IWorldAccess worldAccess, IVertexArrayBuilder builder,String renderType,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ
    ){
        for (Face f:this.faces){
            f.render(worldAccess,builder,this,renderX,renderY,renderZ,worldX,worldY,worldZ,renderType);
        }
    }
}
