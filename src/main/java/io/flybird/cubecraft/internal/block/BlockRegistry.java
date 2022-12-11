package io.flybird.cubecraft.internal.block;

import io.flybird.cubecraft.internal.BlockType;
import io.flybird.cubecraft.register.RegisterUtil;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.block.OverwrittenBlock;
import io.flybird.util.container.namespace.FieldRegistry;
import io.flybird.util.container.namespace.FieldRegistryHolder;
import io.flybird.util.container.namespace.ItemRegisterFunc;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

/**
 * block registry,auto register with field registry
 */
@FieldRegistryHolder(namespace = "cubecraft")
public class BlockRegistry {
    //air
    @FieldRegistry(id = "air")
    public static final Block AIR=new OverwrittenBlock(BlockType.AIR, BlockBehaviorRegistry.AIR);

    //<editor-fold> stone
    @FieldRegistry(id = "stone")
    public static final Block STONE=new OverwrittenBlock(BlockType.STONE, BlockBehaviorRegistry.STONE);

    @FieldRegistry(id = "andesite")
    public static final Block ANDESITE=new OverwrittenBlock(BlockType.ANDESITE, BlockBehaviorRegistry.STONE);

    @FieldRegistry(id = "diorite")
    public static final Block DIORITE=new OverwrittenBlock(BlockType.DIORITE, BlockBehaviorRegistry.STONE);

    @FieldRegistry(id = "granite")
    public static final Block GRANITE=new OverwrittenBlock(BlockType.DIORITE, BlockBehaviorRegistry.STONE);

    @FieldRegistry(id = "granite")
    public static final Block BASALT=new OverwrittenBlock(BlockType.BASALT, BlockBehaviorRegistry.STONE);
    //</editor-fold>

    //<editor-fold> log
    @FieldRegistry(id = "oak_log")
    public static final Block OAK_LOG=new OverwrittenBlock(BlockType.OAK_LOG, BlockBehaviorRegistry.LOG);

    @FieldRegistry(id = "birch_log")
    public static final Block BIRCH_LOG=new OverwrittenBlock(BlockType.BIRCH_LOG, BlockBehaviorRegistry.LOG);

    @FieldRegistry(id="spruce_log")
    public static final Block SPRUCE_LOG=new OverwrittenBlock(BlockType.SPRUCE_LOG, BlockBehaviorRegistry.LOG);

    @FieldRegistry(id = "mangrove_log")
    public static final Block MANGROVE_LOG=new OverwrittenBlock(BlockType.MANGROVE_LOG, BlockBehaviorRegistry.LOG);

    @FieldRegistry(id = "acacia_log")
    public static final Block ACACIA_LOG=new OverwrittenBlock(BlockType.ACACIA_LOG, BlockBehaviorRegistry.LOG);

    @FieldRegistry(id="spruce_log")
    public static final Block DARK_OAK_LOG=new OverwrittenBlock(BlockType.DARK_OAK_LOG, BlockBehaviorRegistry.LOG);
    //</editor-fold>

    //<editor-fold> leaves
    @FieldRegistry(id = "oak_leaves")
    public static final Block OAK_LEAVES=new OverwrittenBlock(BlockType.OAK_LEAVES, BlockBehaviorRegistry.LEAVES);

    @FieldRegistry(id = "birch_leaves")
    public static final Block BIRCH_LEAVES=new OverwrittenBlock(BlockType.BIRCH_LEAVES, BlockBehaviorRegistry.LEAVES);

    @FieldRegistry(id="spruce_leaves")
    public static final Block SPRUCE_LEAVES=new OverwrittenBlock(BlockType.SPRUCE_LEAVES, BlockBehaviorRegistry.LEAVES);

    @FieldRegistry(id = "mangrove_leaves")
    public static final Block MANGROVE_LEAVES=new OverwrittenBlock(BlockType.MANGROVE_LEAVES, BlockBehaviorRegistry.LEAVES);

    @FieldRegistry(id = "acacia_leaves")
    public static final Block ACACIA_LEAVES=new OverwrittenBlock(BlockType.ACACIA_LEAVES, BlockBehaviorRegistry.LEAVES);

    @FieldRegistry(id="dark_oak_leaves")
    public static final Block DARK_OAK_LEAVES=new OverwrittenBlock(BlockType.DARK_OAK_LEAVES, BlockBehaviorRegistry.LEAVES);
    //</editor-fold>


    @ItemRegisterFunc
    public void registerBlockTree(NameSpacedRegisterMap<Block, Block> blockMap) {
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
