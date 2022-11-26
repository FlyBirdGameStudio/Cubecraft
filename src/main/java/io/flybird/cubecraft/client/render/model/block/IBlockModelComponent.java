package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import org.joml.Vector3d;

public abstract class IBlockModelComponent {
    protected final RenderType type;
    protected final Vector3d start,end;



    protected IBlockModelComponent(RenderType type, Vector3d start, Vector3d end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public abstract void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double rendery, double renderz);

    public abstract void renderAsItem(VertexArrayBuilder builder, double renderX, double rendery, double renderz);
}
