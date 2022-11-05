package io.flybird.cubecraft.internal.block;

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
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:air","cubecraft:air",blockMap);


        //behavior:dirt todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:dirt", "cubecraft:dirt", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:coarse_dirt", "cubecraft:dirt", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:coarse_dirt", "cubecraft:dirt", blockMap);

        //behavior:grass-block todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:grass_block", "cubecraft:grass_block", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:podzol", "cubecraft:grass_block", blockMap);
        //RegisterUtil.registerDefaultOverrideBlock("cubecraft:mycelium", "cubecraft:grass_block", blockMap);

        //behavior:stone
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:stone", "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:andesite", "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:granite", "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:diorite", "cubecraft:stone", blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:basalt", "cubecraft:stone", blockMap);

        /*
        //behavior:sand todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:sand","cubecraft:sand",blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:red_sand","cubecraft:sand",blockMap);

        //behavior:sand-stone todo
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:sand_stone","cubecraft:sand_stone",blockMap);
        RegisterUtil.registerDefaultOverrideBlock("cubecraft:red_sand_stone","cubecraft:sand_stone",blockMap);
         */
    }
}
