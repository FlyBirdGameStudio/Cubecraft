package com.sunrisestudio.cubecraft.client.render.worldObjectRenderer;
import com.sunrisestudio.cubecraft.client.render.model.RenderType;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

public interface IBlockRenderer {
    void render(
            BlockState currentBlockState, World world,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ,
            IVertexArrayBuilder builder);

    RenderType getRenderType();

    default void render(
            BlockState currentBlockState, World world, RenderType renderType,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ,
            IVertexArrayBuilder builder
    ){
        if(renderType== getRenderType()){
            render(currentBlockState, world, renderX, renderY, renderZ, worldX, worldY, worldZ, builder);
        }
    }
}
