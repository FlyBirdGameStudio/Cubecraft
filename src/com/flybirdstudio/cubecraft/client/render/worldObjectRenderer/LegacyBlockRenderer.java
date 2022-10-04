package com.flybirdstudio.cubecraft.client.render.worldObjectRenderer;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.textures.Texture2DTileMap;
import org.joml.Vector3d;

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

    public void render(IWorld world, BlockState bs, long x, long y, long z, long renderX, long renderY, long renderZ, EnumFacing facing, VertexArrayBuilder builder) {
        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRender(0,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),0)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c1, c1, c1);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),0));
            this.renderFaceWithTextureId(tex,0,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
        if (this.shouldRender(1,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),1)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c1, c1, c1);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),1));
            this.renderFaceWithTextureId(tex,1,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
        if (this.shouldRender(2,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),2)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c2, c2, c2);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),2));
            this.renderFaceWithTextureId(tex,2,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
        if (this.shouldRender(3,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),3)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c2, c2, c2);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),3));
            this.renderFaceWithTextureId(tex,3,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
        if (this.shouldRender(4,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),4)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c3, c3, c3);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),4));
            this.renderFaceWithTextureId(tex,4,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
        if (this.shouldRender(5,facing,world, x, y, z)) {
            builder.color(getFaceColor(world,bs,x,y,z,EnumFacing.clip(facing.getNumID(),5)));
            if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_classic_lighting",true)) {
                builder.multColorB(c3, c3, c3);
            }
            String tex = this.getTexture(world, bs, x, y, z,EnumFacing.clip(facing.getNumID(),5));
            this.renderFaceWithTextureId(tex,5,builder,world,bs,x,y,z,renderX,renderY,renderZ);
        }
    }

    public boolean shouldRender(int f,EnumFacing facing,IWorld world, long x, long y, long z) {
        return !world.getBlockState(EnumFacing.findNear(x,y,z,1,EnumFacing.clip(facing.getNumID(),f))).getBlock().isSolid();
    }

    public void renderFace(VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ, int face) {
        String tex = this.getTexture(w, bs, x, y, z,face);
        this.renderFaceWithTextureId(tex,face,builder,w,bs,x,y,z,renderX,renderY,renderZ);
    }

    public void renderFaceWithTextureId(String tex,int face,VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ){


        Texture2DTileMap terrain = Registry.getTextureManager().getTexture2DTileMapContainer().get("cubecraft:terrain");
        float u0 = terrain.exactTextureU(tex, 0);
        float u1 = terrain.exactTextureU(tex, 1);
        float v0 = terrain.exactTextureV(tex, 0);
        float v1 = terrain.exactTextureV(tex, 1);

        Vector3d _v0= bs.getFacing().clipVec(new Vector3d(0,0,0));
        Vector3d _v1= bs.getFacing().clipVec(new Vector3d(1,1,1));


        double x0 = (renderX + _v0.x);
        double x1 = (renderX + _v1.x);
        double y0 = (renderY + _v0.y);
        double y1 = (renderY + _v1.y);
        double z0 = (renderZ + _v0.z);
        double z1 = (renderZ + _v1.z);

        if (face == 1) {
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x0, y0, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u1, v0);
            builder.vertexUV(x1, y0, z1, u1, v1);
            return;
        }
        if (face == 0) {
            builder.vertexUV(x1, y1, z1, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y1, z1, u0, v1);
            return;
        }
        if (face == 3) {
            builder.vertexUV(x0, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z0, u1, v1);
            return;
        }
        if (face == 2) {
            builder.vertexUV(x0, y1, z1, u0, v0);
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z1, u1, v1);
            builder.vertexUV(x1, y1, z1, u1, v0);
            return;
        }
        if (face == 5) {
            builder.vertexUV(x0, y1, z1, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z1, u1, v1);
            return;
        }
        if (face == 4) {
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
