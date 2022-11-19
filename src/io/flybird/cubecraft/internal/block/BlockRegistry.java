package io.flybird.cubecraft.internal.block;

import io.flybird.cubecraft.internal.BlockType;
import io.flybird.cubecraft.register.RegisterUtil;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.util.container.namespace.ItemRegisterFunc;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

public class BlockRegistry {

    @ItemRegisterFunc
    public void registerBlockTree(NameSpacedRegisterMap<Block, Block> blockMap) {
        //behavior:log todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:oak_log", "cubecraft:log", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:birch_log", "cubecraft:log", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:spruce_log", "cubecraft:log", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:dark_oak_log", "cubecraft:log", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:acacia_log", "cubecraft:log", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:mangrove_log", "cubecraft:log", blockMap);

        //behavior:leaves todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:oak_leaves", "cubecraft:leaves", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:birch_leaves", "cubecraft:leaves", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:spruce_leaves", "cubecraft:leaves", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:dark_oak_leaves", "cubecraft:leaves", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:acacia_leaves", "cubecraft:leaves", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:mangrove_leaves", "cubecraft:leaves", blockMap);

        //behavior:sapling todo
        /*
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:oak_sapling", "cubecraft:sapling", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:birch_sapling", "cubecraft:sapling", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:spruce_sapling", "cubecraft:sapling", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:dark_oak_sapling", "cubecraft:sapling", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:acacia_sapling", "cubecraft:sapling", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:mangrove_sapling", "cubecraft:sapling", blockMap);

         */
    }

    @ItemRegisterFunc
    public void registerEnvironmentBlock(NameSpacedRegisterMap<Block, Block> blockMap) {
        //behavior:air todo
        RegisterUtil.registerDefaultOverrideBlock(BlockType.AIR,BlockType.AIR,blockMap);


        //behavior:dirt todo
        RegisterUtil.registerDefaultOverrideBlock(BlockType.DIRT, "cubecraft:dirt", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:coarse_dirt", "cubecraft:dirt", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:coarse_dirt", "cubecraft:dirt", blockMap);

        //behavior:grass-block todo
        RegisterUtil.registerDefaultOverrideBlock(BlockType.GRASS_BLOCK, "cubecraft:grass_block", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.PODZOL, "cubecraft:grass_block", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.MYCELIUM, "cubecraft:grass_block", blockMap);


        //behavior:stone
        RegisterUtil.registerDefaultOverrideBlock(BlockType.STONE, "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.ANDESITE, "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.GRANITE, "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.DIORITE, "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.BASALT, "cubecraft:stone", blockMap);

        /*
        //behavior:sand todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:sand","cubecraft:sand",blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:red_sand","cubecraft:sand",blockMap);

        //behavior:sand-stone todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:sand_stone","cubecraft:sand_stone",blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:red_sand_stone","cubecraft:sand_stone",blockMap);
         */
    }

    @ItemRegisterFunc
    public void registerColorBlock(NameSpacedRegisterMap<Block, Block> blockMap) {
        RegisterUtil.registerDefaultOverrideBlock(BlockType.GREEN_STAINED_GLASS, "cubecraft:glass", blockMap);
        RegisterUtil.registerDefaultOverrideBlock(BlockType.BLUE_STAINED_GLASS, "cubecraft:glass", blockMap);
    }
}
