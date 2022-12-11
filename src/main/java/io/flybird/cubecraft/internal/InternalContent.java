package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.internal.ui.component.*;
import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.extansion.event.ModLoadEvent;
import io.flybird.cubecraft.internal.net.packet.PacketRegistry;
import io.flybird.cubecraft.internal.renderer.ChunkRenderer;
import io.flybird.cubecraft.internal.renderer.EntityRenderer;
import io.flybird.cubecraft.internal.renderer.HUDRenderer;
import io.flybird.cubecraft.extansion.*;
import io.flybird.cubecraft.internal.block.BlockBehaviorRegistry;
import io.flybird.cubecraft.internal.block.BlockRegistry;
import io.flybird.cubecraft.internal.world.provider.WorldProviderOverWorld;
import io.flybird.cubecraft.internal.world.worldGen.WorldGeneratorOverworld;

import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.biome.Biome;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.util.logging.LogHandler;


@CubecraftMod(name = "cubecraft-internal-content-pack", version = "1.0")
public class InternalContent extends Mod {
    public LogHandler logger=LogHandler.create("CubecraftInternal/ModMain");;

    public void load(ModLoadEvent e) {
        if(e.client()) {
            logger.info("initialize in client side...");
            this.registerGUIContent();
        }else{
            logger.info("initialize in server side...");
        }
        this.registerContent();
    }

    private void registerGUIContent(){
        Registries.SCREEN_LOADER.registerComponent("button", Button.class, new Button.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("image", ImageRenderer.class, new ImageRenderer.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("label", Label.class, new Label.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("circlewaiting", CircleWaitingAnimation.class, new CircleWaitingAnimation.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("splash", SplashText.class,new SplashText.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("textbar", TextBar.class, new TextBar.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("topbar", TopBar.class,new TopBar.XMLDeserializer());
        Registries.SCREEN_LOADER.registerComponent("panel", Panel.class,new Panel.XMLDeserializer());

        Registries.SCREEN_LOADER.registerLayout("origin", OriginLayout.class,new OriginLayout.XMLDeserializer());
        Registries.SCREEN_LOADER.registerLayout("flow", FlowLayout.class,new FlowLayout.XMLDeserializer());
        Registries.SCREEN_LOADER.registerLayout("viewport", ViewportLayout.class,new ViewportLayout.XMLDeserializer());
    }

    public void registerContent() {
        Registries.WORLD_RENDERER.registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        Registries.WORLD_RENDERER.registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        Registries.WORLD_RENDERER.registerItem("cubecraft:hud_renderer", HUDRenderer.class);

        Registries.BLOCK_BEHAVIOR.registerGetter(BlockBehaviorRegistry.class);
        Registries.BLOCK_BEHAVIOR.registerFieldHolder(BlockBehaviorRegistry.class);
        Registries.BLOCK.registerGetFunctionProvider(BlockRegistry.class);
        Registries.BLOCK.registerFieldHolder(BlockRegistry.class);

        Registries.COLOR_MAP.registerGetter(ColorMapRegistry.class);
        Registries.WORLD_GENERATOR.registerItem(WorldType.OVERWORLD, new ChunkGeneratorPipeline(WorldType.OVERWORLD).add(new WorldGeneratorOverworld()));

        Registries.BIOME.registerItem(BiomeType.PLAINS, new Biome(0, 0, 0, 0, 0, BiomeType.PLAINS, BlockType.STONE, 0x114514, 0x114514) {
            @Override
            public void buildSurface(Chunk primer, int x, int z, double height, long seed) {

            }
        });

        Registries.WORLD_PROVIDER.registerItem(WorldType.OVERWORLD, new WorldProviderOverWorld());

        Registries.PACKET.registerGetFunctionProvider(PacketRegistry.class);
        Registries.SESSION_SERVICE.registerItem("cubecraft:default", new DefaultSessionService());
    }


}
