package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.model.object.Model;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;

import java.util.ArrayList;
import java.util.List;

public final class BlockModel implements Model {
    private final ArrayList<IBlockModelComponent> components;
    private final ArrayList<String> usedTextures;

    public BlockModel(ArrayList<IBlockModelComponent> components, ArrayList<String> usedTextures) {
        this.components = components;
        this.usedTextures = usedTextures;
    }

    public void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz){
        for (IBlockModelComponent model:components){
            if(currentType==model.type) {
                model.render(builder, currentType, world, bs, x, y, z, renderX, rendery, renderz);
            }
        }
    }

    public void renderAsItem(VertexArrayBuilder builder, double renderX, double renderY, double renderZ){
        for (IBlockModelComponent model:components){
            model.renderAsItem(builder, renderX, renderY, renderZ);
        }
    }

    @Override
    public void initializeModel(List<ResourceLocation> textureList) {
        for (String s:this.usedTextures){
            ResourceLocation loc=ResourceLocation.blockTexture(s);
            if(!textureList.contains(loc)) {
                textureList.add(loc);
            }
        }
    }
}
