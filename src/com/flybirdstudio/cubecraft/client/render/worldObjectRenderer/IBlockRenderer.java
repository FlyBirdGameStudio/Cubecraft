package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;
import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public interface IBlockRenderer {

    void render(
            BlockState currentBlockState, IWorld world,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ,
            VertexArrayBuilder builder);

    RenderType getRenderType();

    default void render(
            BlockState currentBlockState, IWorld world, RenderType renderType,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ,
            VertexArrayBuilder builder
    ){
        if(renderType== getRenderType()){
            render(currentBlockState, world, renderX, renderY, renderZ, worldX, worldY, worldZ, builder);
        }
    }
}
