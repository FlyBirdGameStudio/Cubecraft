package com.flybirdstudio.cubecraft.client.render.model.block;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.model.object.Model;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

import java.util.ArrayList;

public final class BlockModel implements Model {
    private final String id,namespace;
    private final ArrayList<IBlockModelComponent> components;

    public BlockModel(String id, String namespace, ArrayList<IBlockModelComponent> components) {
        this.id = id;
        this.namespace = namespace;
        this.components = components;
    }

    public void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz){
        for (IBlockModelComponent model:components){
            if(currentType==model.type) {
                model.render(builder, currentType, world, bs, x, y, z, renderX, rendery, renderz);
            }
        }
    }

    void renderAsItem(VertexArrayBuilder builder, double renderX, double rendery, double renderz){
        for (IBlockModelComponent model:components){
            model.renderAsItem(builder, renderX, rendery, renderz);
        }
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getNameSpace() {
        return null;
    }
}
