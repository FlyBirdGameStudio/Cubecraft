package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.internal.block.BlockType;
import io.flybird.cubecraft.internal.ui.component.*;
import io.flybird.cubecraft.internal.ui.renderer.*;
import io.flybird.cubecraft.internal.ui.layout.*;
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

/**
 * internal content pack
 *
 * @author GrassBlock2022
 */
@CubecraftMod(name = "cubecraft-internal-content-pack", version = "1.0")
public class InternalContent extends Mod {
    public final LogHandler logger = LogHandler.create("CubecraftInternal/ModMain");

    public void load(ModLoadEvent e) {
        if (e.client()) {
            logger.info("initialize in client side...");
            this.registerGUIContent();
        } else {
            logger.info("initialize in server side...");
        }
        this.registerContent();
    }

    private void registerGUIContent() {
        //component
        Registries.GUI_MANAGER.registerComponent("button", Button.class, new Button.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("image", ImageRenderer.class, new ImageRenderer.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("label", Label.class, new Label.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("waiting", WaitingAnimation.class, new WaitingAnimation.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("splash", SplashText.class, new SplashText.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("textbar", TextBar.class, new TextBar.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("topbar", TopBar.class, new TopBar.XMLDeserializer());
        Registries.GUI_MANAGER.registerComponent("panel", Panel.class, new Panel.XMLDeserializer());

        //layout
        Registries.GUI_MANAGER.registerLayout("origin", OriginLayout.class, new OriginLayout.XMLDeserializer());
        Registries.GUI_MANAGER.registerLayout("flow", FlowLayout.class, new FlowLayout.XMLDeserializer());
        Registries.GUI_MANAGER.registerLayout("viewport", ViewportLayout.class, new ViewportLayout.XMLDeserializer());

        //render_controller
        Registries.GUI_MANAGER.registerRenderController(Button.class, ResourceLocation.uiRenderController("cubecraft","button.json"));
        Registries.GUI_MANAGER.registerRenderController(Panel.class, ResourceLocation.uiRenderController("cubecraft","panel.json"));
        Registries.GUI_MANAGER.registerRenderController(TopBar.class, ResourceLocation.uiRenderController("cubecraft","topbar.json"));
        Registries.GUI_MANAGER.registerRenderController(TextBar.class,ResourceLocation.uiRenderController("cubecraft","textbar.json"));
        Registries.GUI_MANAGER.registerRenderController(WaitingAnimation.class, ResourceLocation.uiRenderController("cubecraft", "circlewaiting.json"));

        //renderer_part
        Registries.GUI_MANAGER.registerRendererPart("image_all_boarder", AllBoarderImage.class,new AllBoarderImage.JDeserializer());
        Registries.GUI_MANAGER.registerRendererPart("image_horizontal_boarder", HorizontalBoarderImage.class,new HorizontalBoarderImage.JDeserializer());
        Registries.GUI_MANAGER.registerRendererPart("image_vertical_boarder", VerticalBorderImage.class,new VerticalBorderImage.JDeserializer());
        Registries.GUI_MANAGER.registerRendererPart("font", Font.class,new Font.JDeserializer());
        Registries.GUI_MANAGER.registerRendererPart("image_animation", ImageAnimation.class,new ImageAnimation.JDeserializer());
        Registries.GUI_MANAGER.registerRendererPart("color", Color.class,new Color.JDeserializer());
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
