package io.flybird.cubecraft.client.render.worldObjectRenderer;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.quantum3d.draw.VertexArrayBuilder;

import java.util.List;

public interface IBlockRenderer {

    void renderBlock(
            BlockState currentBlockState, RenderType type, IWorld world,
            double renderX, double renderY, double renderZ,
            long worldX, long worldY, long worldZ,
            VertexArrayBuilder builder);

    default void initializeRenderer(List<ResourceLocation> textureList){}
}
