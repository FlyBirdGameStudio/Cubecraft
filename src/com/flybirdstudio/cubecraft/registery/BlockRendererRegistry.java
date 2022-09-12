package com.flybirdstudio.cubecraft.registery;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.model.RenderType;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.LegacyBlockRendererImpl;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

import java.util.Objects;

public class BlockRendererRegistry {
    @NameSpaceItemGetter(id = "grass_block", namespace = "cubecraft")
    public IBlockRenderer grass_block() {
        return new LegacyBlockRenderer(RenderType.ALPHA) {
            @Override
            public String getTexture(IWorld world, BlockState bs, long x, long y, long z, int face) {
                if (face == 1) {
                    return "/resource/textures/block/grass_block_top.png";
                } else if (face == 0) {
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
                if (face == 1) {
                    return Registery.getBiomeMap().get(bs.getBiome()).getGrassColor();
                } else if (face == 0) {
                    return 0xFFFFFF;
                } else {
                    if (!GameSetting.instance.getValueAsBoolean("client.render.textures.high_quality_grass", false)) {
                        return 0xFFFFFF;
                    }
                    if (face == 2) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z - 1).getId(), "cubecraft:grass_block")) {
                            return Registery.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 3) {
                        if (Objects.equals(world.getBlockState(x, y - 1, z + 1).getId(), "cubecraft:grass_block")) {
                            return Registery.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 4) {
                        if (Objects.equals(world.getBlockState(x - 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return Registery.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    if (face == 5) {
                        if (Objects.equals(world.getBlockState(x + 1, y - 1, z).getId(), "cubecraft:grass_block")) {
                            return Registery.getBiomeMap().get(bs.getBiome()).getGrassColor();
                        }
                    }
                    return 0xFFFFFF;
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
