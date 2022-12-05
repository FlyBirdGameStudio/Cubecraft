package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.internal.net.packet.PacketRegistry;
import io.flybird.cubecraft.internal.renderer.ChunkRenderer;
import io.flybird.cubecraft.internal.renderer.EntityRenderer;
import io.flybird.cubecraft.internal.renderer.HUDRenderer;
import io.flybird.cubecraft.extansion.*;
import io.flybird.cubecraft.internal.block.BlockBehaviorRegistry;
import io.flybird.cubecraft.internal.block.BlockRegistry;
import io.flybird.cubecraft.internal.block.ColorMapRegistry;
import io.flybird.cubecraft.internal.world.provider.WorldProviderOverWorld;
import io.flybird.cubecraft.internal.world.worldGen.WorldGeneratorOverworld;
import io.flybird.cubecraft.register.ContentRegistry;

import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.register.RenderRegistry;
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
        RenderRegistry.getWorldRenderers().registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        RenderRegistry.getWorldRenderers().registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        RenderRegistry.getWorldRenderers().registerItem("cubecraft:hud_renderer", HUDRenderer.class);

        ContentRegistry.getBlockBehaviorMap().registerGetter(BlockBehaviorRegistry.class);
        ContentRegistry.getBlockMap().registerGetFunctionProvider(BlockRegistry.class);
        RenderRegistry.getColorMaps().registerGetter(ColorMapRegistry.class);
        ContentRegistry.getWorldGeneratorMap().registerItem(WorldType.OVERWORLD, new ChunkGeneratorPipeline(WorldType.OVERWORLD).add(new WorldGeneratorOverworld()));


        ContentRegistry.getBiomeMap().registerItem(BiomeType.PLAINS, new Biome(0,0,0,0,0,BiomeType.PLAINS,BlockType.STONE,0x114514,0x114514) {
            @Override
            public void buildSurface(Chunk primer, int x, int z, double height, long seed) {

            }
        });

        ContentRegistry.getWorldIdList().add(WorldType.OVERWORLD);

        ContentRegistry.getWorldProviderMap().registerItem(WorldType.OVERWORLD,new WorldProviderOverWorld());

        Registry.getPackets().registerGetFunctionProvider(PacketRegistry.class);


        Registry.getSessionServiceMap().registerItem("cubecraft:default",new DefaultSessionService());
    }
}
