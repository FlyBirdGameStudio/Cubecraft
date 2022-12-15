package io.flybird.cubecraft.client.render.worldObjectRenderer;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;

import java.util.List;

public class BlockModelRenderer implements IBlockRenderer{
    private final String model;

    public BlockModelRenderer(String id,String model) {
        this.model = model;
    }

    public BlockModelRenderer(ResourceLocation loc) {
        this.model = loc.format();
    }

    public String getModel() {
        return model;
    }

    @Override
    public void renderBlock(BlockState currentBlockState, RenderType type, IWorld world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, VertexArrayBuilder builder) {
        ClientRegistries.BLOCK_MODEL.get(model).render(builder,type,world,currentBlockState,worldX,worldY,worldZ,renderX,renderY,renderZ);
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> textureList) {
        ClientRegistries.BLOCK_MODEL.load(model);
        ClientRegistries.BLOCK_MODEL.get(model).initializeModel(textureList);
    }
}
