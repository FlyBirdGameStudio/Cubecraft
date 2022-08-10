package com.sunrisestudio.cubecraft.client.render.worldObjectRenderer;

import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

public class BlockModelRenderer implements IBlockRenderer{
    @Override
    public void render(BlockState currentBlockState, IWorldAccess world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, IVertexArrayBuilder builder) {
        //todo:block model dispatch and render
    }

    @Override
    public RenderType getRenderType(){
        //todo:add block model render type
        return RenderType.ALPHA;
    }
}
