package com.sunrisestudio.cubecraft.registery;

import com.sunrisestudio.cubecraft.client.render.model.FaceTypeAdapterRenderer;
import com.sunrisestudio.cubecraft.client.render.model.object.ModelObject;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.container.namespace.NameSpaceItemGetter;

public class FaceRenderers {

    //cross
    @NameSpaceItemGetter(id = "cross_0",namespace="cubecraft")
    public FaceTypeAdapterRenderer cross_0(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x0, y1, z0, u1, v0, l);
            builder.vertexUV(x1, y1, z1, u0, v0, l);
            builder.vertexUV(x1, y0, z1, u0, v1, l);
            builder.vertexUV(x0, y0, z0, u1, v1, l);

            builder.vertexUV(x1, y1, z1, u1, v0, l);
            builder.vertexUV(x0, y1, z0, u0, v0, l);
            builder.vertexUV(x0, y0, z0, u0, v1, l);
            builder.vertexUV(x1, y0, z1, u1, v1, l);
        };
    }

    @NameSpaceItemGetter(id = "cross_1",namespace="cubecraft")
    public FaceTypeAdapterRenderer cross_1(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x1, y1, z0, u1, v0, l);
            builder.vertexUV(x0, y1, z1, u0, v0, l);
            builder.vertexUV(x0, y0, z1, u0, v1, l);
            builder.vertexUV(x1, y0, z0, u1, v1, l);
            builder.vertexUV(x0, y1, z1, u1, v0, l);
            builder.vertexUV(x1, y1, z0, u0, v0, l);
            builder.vertexUV(x1, y0, z0, u0, v1, l);
            builder.vertexUV(x0, y0, z1, u1, v1, l);
        };
    }

    //cube
    @NameSpaceItemGetter(id = "cube_0",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_0(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x0, y0, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u1, v0);
            builder.vertexUV(x1, y0, z1, u1, v1);
        };
    }

    @NameSpaceItemGetter(id = "cube_1",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_1(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x1, y1, z1, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y1, z1, u0, v1);
        };
    }

    @NameSpaceItemGetter(id = "cube_2",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_2(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x0, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z0, u1, v1);
        };
    }

    @NameSpaceItemGetter(id = "cube_3",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_3(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x0, y1, z1, u0, v0);
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z1, u1, v1);
            builder.vertexUV(x1, y1, z1, u1, v0);
        };
    }

    @NameSpaceItemGetter(id = "cube_4",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_4(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x0, y1, z1, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z1, u1, v1);
        };
    }

    @NameSpaceItemGetter(id = "cube_5",namespace="cubecraft")
    public FaceTypeAdapterRenderer cube_5(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {
            builder.vertexUV(x1, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z0, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z1, u0, v0);
        };
    }




    @NameSpaceItemGetter(id = "cube_fxxk",namespace="cubecraft")
    public FaceTypeAdapterRenderer fxxk(){
        return (worldAccess, builder, modelObject, x0, x1, y0, y1, z0, z1, u0, u1, v0, v1, l, worldX, worldY, worldZ) -> {

        };
    }
}
