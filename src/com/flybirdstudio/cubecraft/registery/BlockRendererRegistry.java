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
                } else {
                    if (!GameSetting.instance.getValueAsBoolean("client.render.textures.high_quality_grass", false)) {
                        return "/resource/textures/block/grass_block_side.png";
                    }
                    if (face == 2) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z - 1).getId(), "cubecraft:grass_block")) {
                            return "/resource/textures/block/grass_block_top.png";
                        }
                    }
                    if (face == 3) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z + 1).getId(), "cubecraft:grass_block")) {
                            return "/resource/textures/block/grass_block_top.png";
                        }
                    }
                    if (face == 4) {
                        if (Objects.equals(world.getBlockState(x - 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return "/resource/textures/block/grass_block_top.png";
                        }
                    }
                    if (face == 5) {
                        if (Objects.equals(world.getBlockState(x + 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return "/resource/textures/block/grass_block_top.png";
                        }
                    }
                    return "/resource/textures/block/grass_block_side.png";
                }
            }

            @Override
            public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
                if (face == 0) {
                    return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                } else if (face == 1) {
                    return 0xFFFFFF;
                } else if(face==6||face==7||face==8||face==9){
                    return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                }else{
                    if (!GameSetting.instance.getValueAsBoolean("client.render.textures.high_quality_grass", false)) {
                        return 0xFFFFFF;
                    }
                    if (face == 2) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z - 1).getId(), "cubecraft:grass_block")) {
                            return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 3) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z + 1).getId(), "cubecraft:grass_block")) {
                            return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 4) {
                        if (Objects.equals(world.getBlockState(x - 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 5) {
                        if (Objects.equals(world.getBlockState(x + 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return Registry.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    return 0xFFFFFF;
                }
            }

            @Override
            public void render(IWorld world, BlockState bs, long x, long y, long z, long renderX, long renderY, long renderZ, EnumFacing facing, VertexArrayBuilder builder) {
                super.render(world, bs, x, y, z, renderX, renderY, renderZ, facing, builder);

                if(!GameSetting.instance.getValueAsBoolean("client.render.textures.high_quality_grass", false)) {
                    byte c2 = -52;
                    byte c3 = -103;
                    if (this.shouldRender(2,facing,world, x, y, z)) {
                        builder.color(getFaceColor(world, bs, x, y, z, 0));
                        builder.multColorB(c2, c2, c2);
                        this.renderFaceWithTextureId("/resource/textures/block/grass_block_side_overlay.png", 2,
                                builder, world, bs, x, y, z, renderX, renderY, renderZ);
                    }
                    if (this.shouldRender(3,facing,world, x, y, z)) {
                        builder.color(getFaceColor(world, bs, x, y, z, 0));
                        builder.multColorB(c2, c2, c2);
                        this.renderFaceWithTextureId("/resource/textures/block/grass_block_side_overlay.png", 3,
                                builder, world, bs, x, y, z, renderX, renderY, renderZ);
                    }
                    if (this.shouldRender(4,facing,world, x, y, z)) {
                        builder.color(getFaceColor(world, bs, x, y, z, 0));
                        builder.multColorB(c3, c3, c3);
                        this.renderFaceWithTextureId("/resource/textures/block/grass_block_side_overlay.png", 4,
                                builder, world, bs, x, y, z, renderX, renderY, renderZ);
                    }
                    if (this.shouldRender(5,facing,world, x, y, z)) {
                        builder.color(getFaceColor(world, bs, x, y, z, 0));
                        builder.multColorB(c3, c3, c3);
                        this.renderFaceWithTextureId("/resource/textures/block/grass_block_side_overlay.png", 5,
                                builder, world, bs, x, y, z, renderX, renderY, renderZ);
                    }
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
