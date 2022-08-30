package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public class BlockModelRenderer implements IBlockRenderer{
    @Override
    public void render(BlockState currentBlockState, IWorld world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, VertexArrayBuilder builder) {
        //todo:block model dispatch and render
    }

    @Override
    public RenderType getRenderType(){
        //todo:add block model render type
        return RenderType.ALPHA;
    }
}
