package com.flybirdstudio.cubecraft.client.render.model.block;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import org.joml.Vector3d;

public abstract class IBlockModelComponent {
    protected final RenderType type;
    protected final Vector3d start,end;
    protected final String colorMap;
    protected final CullingMethod culling;


    protected IBlockModelComponent(RenderType type, Vector3d start, Vector3d end, String colorMap, CullingMethod culling) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.colorMap = colorMap;
        this.culling = culling;
    }

    public abstract void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz);

    public abstract void renderAsItem(VertexArrayBuilder builder, double renderX, double rendery, double renderz);
}
