package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.extansion.*;
import io.flybird.cubecraft.internal.block.BlockBehaviorRegistry;
import io.flybird.cubecraft.internal.block.BlockRegistry;
import io.flybird.cubecraft.internal.block.ColorMapRegistry;
import io.flybird.cubecraft.internal.worldGen.WorldGeneratorOverworld;
import io.flybird.cubecraft.register.Registry;

import io.flybird.cubecraft.world.biome.Biome;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;

@CubecraftMod(name = "cubecraft-internal-content-pack",version="1.0")
public class InternalContent extends Mod {

    public InternalContent(PlatformClient client, PlatformServer server, ExtansionRunningTarget target) {
        super(client, server, target);
    }

    @Override
    public void construct() {
        Registry.getBlockBehaviorMap().registerGetter(BlockBehaviorRegistry.class);
        Registry.getBlockMap().registerGetFunctionProvider(BlockRegistry.class);
        Registry.getColorMaps().registerGetter(ColorMapRegistry.class);
        Registry.getWorldGeneratorMap().registerItem(WorldType.OVERWORLD, new ChunkGeneratorPipeline(WorldType.OVERWORLD).add(new WorldGeneratorOverworld()));


        Registry.getBiomeMap().registerItem(BiomeType.PLAINS, new Biome(0,0,0,0,0,BiomeType.PLAINS,BlockType.STONE,0x114514,0x114514) {
            @Override
            public void buildSurface(Chunk primer, int x, int z, double height, long seed) {

            }
        });


        Registry.getWorldIdList().add(WorldType.OVERWORLD);
    }
}
