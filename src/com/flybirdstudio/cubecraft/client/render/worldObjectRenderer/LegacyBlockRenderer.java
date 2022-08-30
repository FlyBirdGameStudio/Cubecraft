package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;

public abstract class LegacyBlockRenderer implements IBlockRenderer{
    public final RenderType renderType;
    protected LegacyBlockRenderer(RenderType renderType) {
        this.renderType = renderType;
    }

    public abstract int getTexture(int face);

    @Override
    public void render(BlockState currentBlockState, IWorld world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, VertexArrayBuilder builder) {
        this.render(world,worldX,worldY,worldZ, (long) renderX, (long) renderY, (long) renderZ,currentBlockState.getFacing(),builder);
    }

    @Override
    public RenderType getRenderType() {
        return this.renderType;
    }

    public void render(IWorld world, long x, long y, long z, long renderX, long renderY, long renderZ, BlockFacing facing, VertexArrayBuilder builder) {
        //Registry.getBlockModelManager().get(this.id).render(builder,world,renderX,renderY,renderZ,x,y,z,facing);

        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRender(world, x, y - 1, z)) {
            builder.colorB(c1, c1, c1);
            this.renderFace(builder, renderX, renderY, renderZ, 0);
        }
        if (this.shouldRender(world, x, y + 1, z)) {
            builder.colorB(c1, c1, c1);
            this.renderFace(builder, renderX, renderY, renderZ, 1);
        }
        if (this.shouldRender(world, x, y, z - 1)) {
            builder.colorB(c2, c2, c2);
            this.renderFace(builder, renderX, renderY, renderZ, 2);
        }
        if (this.shouldRender(world, x, y, z + 1)) {
            builder.colorB(c2, c2, c2);
            this.renderFace(builder, renderX, renderY, renderZ, 3);
        }
        if (this.shouldRender(world, x - 1, y, z)) {
            builder.colorB(c3, c3, c3);
            this.renderFace(builder, renderX, renderY, renderZ, 4);
        }
        if (this.shouldRender(world, x + 1, y, z)) {
            builder.colorB(c3, c3, c3);
            this.renderFace(builder, renderX, renderY, renderZ, 5);
        }
    }

    public boolean shouldRender(IWorld world, long x, long y, long z) {
        return !world.getBlockState(x, y, z).getBlock().isSolid();
    }

    public void renderFace(VertexArrayBuilder builder, long x, long y, long z, int face) {
        int tex = this.getTexture(face);
        int xt = tex % 16 * 16;
        int yt = tex / 16 * 16;
        float u0 = (float) xt / 256.0f;
        float u1 = ((float) xt + 15.99f) / 256.0f;
        float v0 = (float) yt / 256.0f;
        float v1 = ((float) yt + 15.99f) / 256.0f;
        double x0 = (x + 0);
        double x1 = (x + 1);
        double y0 = (y + 0);
        double y1 = (y + 1);
        double z0 = (z + 0);
        double z1 = (z + 1);
        if (face == 0) {
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x0, y0, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u1, v0);
            builder.vertexUV(x1, y0, z1, u1, v1);
            return;
        }
        if (face == 1) {
            builder.vertexUV(x1, y1, z1, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y1, z1, u0, v1);
            return;
        }
        if (face == 2) {
            builder.vertexUV(x0, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z0, u1, v1);
            return;
        }
        if (face == 3) {
            builder.vertexUV(x0, y1, z1, u0, v0);
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z1, u1, v1);
            builder.vertexUV(x1, y1, z1, u1, v0);
            return;
        }
        if (face == 4) {
            builder.vertexUV(x0, y1, z1, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z1, u1, v1);
            return;
        }
        if (face == 5) {
            builder.vertexUV(x1, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z0, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z1, u0, v0);
        }
    }
}
