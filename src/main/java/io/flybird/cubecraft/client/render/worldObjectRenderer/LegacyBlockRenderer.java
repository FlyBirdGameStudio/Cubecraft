package io.flybird.cubecraft.client.render.worldObjectRenderer;

import io.flybird.cubecraft.client.render.BlockRenderUtil;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.model.object.Vertex;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.register.RenderRegistry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.textures.Texture2DTileMap;
import io.flybird.util.ColorUtil;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.List;

public abstract class LegacyBlockRenderer implements IBlockRenderer {
    public final RenderType renderType;
    private final ResourceLocation[] usedTextures;

    public LegacyBlockRenderer(RenderType renderType, ResourceLocation... usedTextures) {
        this.renderType = renderType;
        this.usedTextures = usedTextures;
    }

    public abstract String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face);

    @Override
    public void renderBlock(BlockState currentBlockState, RenderType type, IWorld world, double renderX, double renderY, double renderZ, long worldX, long worldY, long worldZ, VertexArrayBuilder builder) {
        if(type==this.renderType) {
            EnumFacing facing = currentBlockState.getFacing();
            if (this.shouldRender(0, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 0));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,0);
                this.renderFaceWithColor(tex,c,0, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
            if (this.shouldRender(1, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 1));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,1);
                this.renderFaceWithColor(tex,c,1, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
            if (this.shouldRender(2, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 2));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,2);
                this.renderFaceWithColor(tex,c,2, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
            if (this.shouldRender(3, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 3));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,3);
                this.renderFaceWithColor(tex,c,3, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
            if (this.shouldRender(4, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 4));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,4);
                this.renderFaceWithColor(tex,c,4, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
            if (this.shouldRender(5, facing, world, worldX, worldY, worldZ)) {
                String tex = this.getTexture(world, currentBlockState, worldX, worldY, worldZ, EnumFacing.clip(facing.getNumID(), 5));
                int c=getFaceColor(world, currentBlockState, worldX, worldY, worldZ,5);
                this.renderFaceWithColor(tex,c,5, builder, world, currentBlockState, worldX, worldY, worldZ, renderX, renderY, renderZ);
            }
        }
    }

    public void renderFaceWithColor(String tex, int c,int face,VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {
        Texture2DTileMap terrain = RenderRegistry.getTextureManager().getTexture2DTileMapContainer().get("cubecraft:terrain");
        float u0 = terrain.exactTextureU(tex, 0);
        float u1 = terrain.exactTextureU(tex, 1);
        float v0 = terrain.exactTextureV(tex, 0);
        float v1 = terrain.exactTextureV(tex, 1);

        Vector3d v000=bs.getFacing().clipVec(new Vector3d(0, 0, 0));
        Vector3d v001=bs.getFacing().clipVec(new Vector3d(0, 0, 1));
        Vector3d v010=bs.getFacing().clipVec(new Vector3d(0, 1, 0));
        Vector3d v011=bs.getFacing().clipVec(new Vector3d(0, 1, 1));
        Vector3d v100=bs.getFacing().clipVec(new Vector3d(1, 0, 0));
        Vector3d v101=bs.getFacing().clipVec(new Vector3d(1, 0, 1));
        Vector3d v110=bs.getFacing().clipVec(new Vector3d(1, 1, 0));
        Vector3d v111=bs.getFacing().clipVec(new Vector3d(1, 1, 1));

        Vector3d render=new Vector3d(renderX,renderY,renderZ);

        if (face == 0) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v111.add(render), new Vector2d(u1, v1), faceColor),v111,w,x,y,z,0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v110.add(render), new Vector2d(u1, v0), faceColor),v110,w,x,y,z,0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v010.add(render), new Vector2d(u0, v0), faceColor),v010,w,x,y,z,0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v011.add(render), new Vector2d(u0, v1), faceColor),v011,w,x,y,z,0).draw(builder);
            return;
        }

        if (face == 1) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v001.add(render), new Vector2d(u0, v1), faceColor),v001,w,x,y,z,1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v000.add(render), new Vector2d(u0, v0), faceColor),v000,w,x,y,z,1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v100.add(render), new Vector2d(u1, v0), faceColor),v100,w,x,y,z,1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v101.add(render), new Vector2d(u1, v1), faceColor),v101,w,x,y,z,1).draw(builder);
            return;
        }

        if (face == 2) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v011.add(render), new Vector2d(u0, v0), faceColor),v011,w,x,y,z,2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v001.add(render), new Vector2d(u0, v1), faceColor),v001,w,x,y,z,2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v101.add(render), new Vector2d(u1, v1), faceColor),v101,w,x,y,z,2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v111.add(render), new Vector2d(u1, v0), faceColor),v111,w,x,y,z,2).draw(builder);
            return;
        }


        if (face == 3) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v010.add(render), new Vector2d(u1, v0), faceColor),v010,w,x,y,z,3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v110.add(render), new Vector2d(u0, v0), faceColor),v110,w,x,y,z,3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v100.add(render), new Vector2d(u0, v1), faceColor),v100,w,x,y,z,3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v000.add(render), new Vector2d(u1, v1), faceColor),v000,w,x,y,z,3).draw(builder);
            return;
        }

        if (face == 4) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v101.add(render), new Vector2d(u0, v1), faceColor),v110,w,x,y,z,4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v100.add(render), new Vector2d(u1, v1), faceColor),v111,w,x,y,z,4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v110.add(render), new Vector2d(u1, v0), faceColor),v101,w,x,y,z,4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v111.add(render), new Vector2d(u0, v0), faceColor),v100,w,x,y,z,4).draw(builder);
            return;
        }

        if (face == 5) {
            Vector3d faceColor=new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(v011.add(render), new Vector2d(u1, v0), faceColor),v011,w,x,y,z,5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v010.add(render), new Vector2d(u0, v0), faceColor),v010,w,x,y,z,5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v000.add(render), new Vector2d(u0, v1), faceColor),v000,w,x,y,z,5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(v001.add(render), new Vector2d(u1, v1), faceColor),v001,w,x,y,z,5).draw(builder);
        }
    }

    public boolean shouldRender(int f, EnumFacing facing, IWorld world, long x, long y, long z) {
        return !world.getBlockState(EnumFacing.findNear(x, y, z, 1, EnumFacing.clip(facing.getNumID(), f))).getBlock().isSolid();
    }

    public void renderFace(VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ, int face) {
        String tex = this.getTexture(w, bs, x, y, z, face);
        this.renderFaceWithTextureId(tex, face, builder, w, bs, x, y, z, renderX, renderY, renderZ);
    }

    public void renderFaceWithTextureId(String tex, int face, VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {

    }

    public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
        return 0xFFFFFF;
    }

    @Override
    public void initializeRenderer(List<ResourceLocation> textureList) {
        textureList.addAll(List.of(this.usedTextures));
    }
}
