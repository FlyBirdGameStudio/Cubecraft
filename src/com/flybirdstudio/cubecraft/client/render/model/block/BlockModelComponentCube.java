package com.flybirdstudio.cubecraft.client.render.model.block;

import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.textures.Texture2DTileMap;
import org.joml.Vector3d;

import java.util.Objects;

public class BlockModelComponentCube extends IBlockModelComponent {
    private final BlockModelFace top, bottom, left, right, front, back;

    public BlockModelComponentCube(RenderType renderType, Vector3d start, Vector3d end, CullingMethod cullingMethod, String colorMap, BlockModelFace top1, BlockModelFace bottom1, BlockModelFace left1, BlockModelFace right1, BlockModelFace front1, BlockModelFace back1) {
        super(renderType, start, end, colorMap, cullingMethod);
        this.top = top1;
        this.bottom = bottom1;
        this.left = left1;
        this.right = right1;
        this.front = front1;
        this.back = back1;
    }

    @Override
    public void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {
        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRender(world, bs, EnumFacing.Down,x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 0));
            builder.multColorB(c1, c1, c1);
            this.renderFace(bottom, 0, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
        if (this.shouldRender(world, bs, EnumFacing.Up, x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 1));
            builder.multColorB(c1, c1, c1);
            this.renderFace(top, 1, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
        if (this.shouldRender(world, bs, EnumFacing.West, x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 2));
            builder.multColorB(c2, c2, c2);
            this.renderFace(left, 2, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
        if (this.shouldRender(world, bs, EnumFacing.East, x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 3));
            builder.multColorB(c2, c2, c2);
            this.renderFace(right, 3, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
        if (this.shouldRender(world, bs, EnumFacing.North, x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 4));
            builder.multColorB(c3, c3, c3);
            this.renderFace(front, 4, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
        if (this.shouldRender(world, bs, EnumFacing.South, x, y, z)) {
            builder.color(getFaceColor(world, bs, x, y, z, 5));
            builder.multColorB(c3, c3, c3);
            this.renderFace(back, 5, builder, world, bs, x, y, z, renderX, renderY, renderZ);
        }
    }

    public boolean shouldRender(IWorld world, BlockState bs, EnumFacing current, long x, long y, long z) {
        EnumFacing actuallyFacing = EnumFacing.clip(current, bs.getFacing());
        return switch (this.culling) {
            case NEVER -> false;
            case SOLID -> !world.getBlockState(actuallyFacing.findNear(x, y, z, 1)).getBlock().isSolid();
            case ALWAYS -> true;
            case EQUALS ->
                    !(Objects.equals(world.getBlockState(actuallyFacing.findNear(x, y, z, 1)).getId(), bs.getId()));
        };
    }

    public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
        return 0xFFFFFF;
    }


    public void renderFace(BlockModelFace f, int face, VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {
        Texture2DTileMap terrain = Registry.getTextureManager().getTexture2DTileMapContainer().get("cubecraft:terrain");

        float u0 = terrain.exactTextureU(f.tex(), f.u0());
        float u1 = terrain.exactTextureU(f.tex(), f.u1());
        float v0 = terrain.exactTextureV(f.tex(), f.v0());
        float v1 = terrain.exactTextureV(f.tex(), f.u1());

        Vector3d _v0= new Vector3d(start);
        Vector3d _v1= new Vector3d(end);


        double x0 = (renderX + _v0.x);
        double x1 = (renderX + _v1.x);
        double y0 = (renderY + _v0.y);
        double y1 = (renderY + _v1.y);
        double z0 = (renderZ + _v0.z);
        double z1 = (renderZ + _v1.z);

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

    @Override
    public void renderAsItem(VertexArrayBuilder builder, double renderX, double rendery, double renderz) {

    }
}
