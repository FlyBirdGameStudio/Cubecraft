package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.textures.Texture2DTileMap;

public abstract class LegacyBlockRenderer implements IBlockRenderer {
    public final RenderType renderType;

    public LegacyBlockRenderer(RenderType renderType) {
        this.renderType = renderType;
    }

    public abstract String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face);

    @Override
    public void render(BlockState currentBlockState, IWorld world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, VertexArrayBuilder builder) {
        this.render(world,currentBlockState, worldX, worldY, worldZ, (long) renderX, (long) renderY, (long) renderZ, currentBlockState.getFacing(), builder);
    }

    @Override
    public RenderType getRenderType() {
        return this.renderType;
    }

    public void render(IWorld world, BlockState bs, long x, long y, long z, long renderX, long renderY, long renderZ, BlockFacing facing, VertexArrayBuilder builder) {
        //Registry.getBlockModelManager().get(this.id).render(builder,world,renderX,renderY,renderZ,x,y,z,facing);

        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRender(world, x, y - 1, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,0));
            builder.multColorB(c1, c1, c1);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 0);
        }
        if (this.shouldRender(world, x, y + 1, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,1));
            builder.multColorB(c1, c1, c1);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 1);
        }
        if (this.shouldRender(world, x, y, z - 1)) {
            builder.color(getFaceColor(world,bs,x,y,z,2));
            builder.multColorB(c2, c2, c2);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 2);
        }
        if (this.shouldRender(world, x, y, z + 1)) {
            builder.color(getFaceColor(world,bs,x,y,z,3));
            builder.multColorB(c2, c2, c2);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 3);
        }
        if (this.shouldRender(world, x - 1, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,4));
            builder.multColorB(c3, c3, c3);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 4);
        }
        if (this.shouldRender(world, x + 1, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,5));
            builder.multColorB(c3, c3, c3);
            this.renderFace(builder, world,bs, x, y, z, renderX, renderY, renderZ, 5);
        }
    }

    public boolean shouldRender(IWorld world, long x, long y, long z) {
        return !world.getBlockState(x, y, z).getBlock().isSolid();
    }

    public void renderFace(VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ, int face) {
        Texture2DTileMap terrain = Registery.getTextureManager().getTexture2DTileMapContainer().get("cubecraft:terrain");
        String tex = this.getTexture(w, bs, x, y, z, face);
        float u0 = terrain.exactTextureU(tex, 0);
        float u1 = terrain.exactTextureU(tex, 1);
        float v0 = terrain.exactTextureV(tex, 0);
        float v1 = terrain.exactTextureV(tex, 1);
        double x0 = (renderX + 0);
        double x1 = (renderX + 1);
        double y0 = (renderY + 0);
        double y1 = (renderY + 1);
        double z0 = (renderZ + 0);
        double z1 = (renderZ + 1);
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

    public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
        return 0xFFFFFF;
    }
}
