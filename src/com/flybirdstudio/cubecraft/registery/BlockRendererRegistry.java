package com.flybirdstudio.cubecraft.registery;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRendererImpl;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

import java.util.Objects;

public class BlockRendererRegistry {
    @NameSpaceItemGetter(id = "grass_block", namespace = "cubecraft")
    public IBlockRenderer grass_block() {
        return new LegacyBlockRenderer(RenderType.ALPHA) {
            @Override
            public String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face) {
                if (face == 0) {
                    return "/resource/textures/block/grass_block_top.png";
                } else if (face == 1) {
                    return "/resource/textures/block/dirt.png";
                }else if(face>1&&face<6){
                    return "/resource/textures/block/grass_block_side.png";
                }else{
                    return "/resource/textures/block/grass_block_side_overlay.png";
                }
            }

            @Override
            public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
                if (face>1&&face<6) {
                    return 0xFFFFFF;
                } else if(face==1){
                    return 0xFFFFFF;
                }else {
                    return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                }
            }


            @Override
            public void render(BlockState bs, IWorld world, RenderType renderType, double renderX, double renderY, double renderZ, long x, long y, long z, VertexArrayBuilder builder) {
                this.render(world,bs, x, y, z,renderX, renderY, renderZ,bs.getFacing(), builder);
                EnumFacing facing=bs.getFacing();
                int c=getFaceColor(world,bs,x,y,z,0);
                String tex = "/resource/textures/block/grass_block_side_overlay.png";
                if (this.shouldRender(2, facing, world, x, y, z)) {
                    this.renderFaceWithColor(tex,c,2,builder, world, bs, x, y, z, renderX, renderY, renderZ-0.001);
                }
                if (this.shouldRender(3, facing, world, x, y, z)) {
                    this.renderFaceWithColor(tex,c,3,builder, world, bs, x, y, z, renderX, renderY, renderZ+0.001);
                }
                if (this.shouldRender(4, facing, world, x, y, z)) {
                    this.renderFaceWithColor(tex,c,4,builder, world, bs, x, y, z, renderX, renderY, renderZ);
                }
                if (this.shouldRender(5, facing, world, x, y, z)) {
                    this.renderFaceWithColor(tex,c,5,builder, world, bs, x, y, z, renderX+0.001, renderY, renderZ);
                }
            }
        };
    }

    @NameSpaceItemGetter(id = "stone", namespace = "cubecraft")
    public IBlockRenderer stone() {
        return new LegacyBlockRendererImpl("/resource/textures/block/stone.png");
    }

    @NameSpaceItemGetter(id = "dirt", namespace = "cubecraft")
    public IBlockRenderer dirt() {
        return new LegacyBlockRendererImpl("/resource/textures/block/dirt.png");
    }

    @NameSpaceItemGetter(id = "air", namespace = "cubecraft")
    public IBlockRenderer air() {
        return null;
    }



}
