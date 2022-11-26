package io.flybird.cubecraft.register;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.IWorldProvider;
import io.flybird.cubecraft.world.biome.BiomeMap;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.item.Item;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

import java.util.ArrayList;

public class ContentRegistry {
    private static final NameSpacedRegisterMap<Block, ?> blockBehaviorMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<Block, Block> blockMap = new NameSpacedRegisterMap<>(blockBehaviorMap);
    private static final NameSpacedConstructingMap<Entity> entityMap = new NameSpacedConstructingMap<>(IWorld.class);
    private static final NameSpacedRegisterMap<ChunkGeneratorPipeline, ?> worldGeneratorMap = new NameSpacedRegisterMap<>(null);
    private static final BiomeMap biomeMap = new BiomeMap();
    private static final NameSpacedRegisterMap<Item, ?> itemMap = new NameSpacedRegisterMap<>(null);
    private static final ArrayList<String> worldIdList = new ArrayList<>();
    private static final NameSpacedRegisterMap<IWorldProvider ,?>worldProviderMap=new NameSpacedRegisterMap<>(null);

    private ContentRegistry() {
        throw new RuntimeException("you should not create instance of this!");
    }

    public static NameSpacedRegisterMap<Block, ?> getBlockBehaviorMap() {
        return blockBehaviorMap;
    }

    public static NameSpacedRegisterMap<Block, Block> getBlockMap() {
        return blockMap;
    }

    public static NameSpacedConstructingMap<Entity> getEntityMap() {
        return entityMap;
    }

    public static NameSpacedRegisterMap<ChunkGeneratorPipeline, ?> getWorldGeneratorMap() {
        return worldGeneratorMap;
    }

    public static BiomeMap getBiomeMap() {
        return biomeMap;
    }

    public static NameSpacedRegisterMap<Item, ?> getItemMap() {
        return itemMap;
    }

    public static ArrayList<String> getWorldIdList() {
        return worldIdList;
    }

    public static NameSpacedRegisterMap<IWorldProvider, ?> getWorldProviderMap() {
        return worldProviderMap;
    }
}
