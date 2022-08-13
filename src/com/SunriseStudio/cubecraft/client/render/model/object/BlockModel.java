package com.sunrisestudio.cubecraft.client.render.model.object;


import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.client.render.model.FaceCullingMethod;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import org.joml.Vector3d;

public record BlockModel (
        String id,
        String namespace,
        RenderType renderType,
        Cube[] objects
        )implements Model {

    /**
     * @param builder vertex array builder
     * @param rx render at x
     * @param ry render at y
     * @param rz render at z
     * @param wx culling at x
     * @param wy culling at y
     * @param wz culling at z
     */
    public void render(IVertexArrayBuilder builder, IWorldAccess world, long rx, long ry, long rz, long wx, long wy, long wz, BlockFacing facing){
        for (int i=0;i<objects.length;i++){
            Cube obj=objects[i];
            boolean[] shouldRender=new boolean[6];
            for (int f = 0; f < 6; f++) {
                BlockFacing face= BlockFacing.clip(facing, BlockFacing.fromId(f));
                shouldRender[f]= FaceCullingMethod.shouldRender(world,wx,wy,wz,id,face,obj.getFace(i).culling());
            }
            obj.render(new Vector3d(rx,ry,rz),BlockFacing.getMatrix(facing),builder,shouldRender);
        }
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getNameSpace() {
        return namespace;
    }
}
