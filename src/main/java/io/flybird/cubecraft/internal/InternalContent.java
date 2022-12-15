package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.extansion.CubecraftMod;
import io.flybird.cubecraft.extansion.Mod;
import io.flybird.cubecraft.extansion.event.ContentInitializeEvent;
import io.flybird.cubecraft.extansion.event.GUIInitializeEvent;
import io.flybird.cubecraft.internal.registry.*;
import io.flybird.cubecraft.internal.type.BiomeType;
import io.flybird.cubecraft.internal.type.BlockType;
import io.flybird.cubecraft.internal.renderer.EntityRenderer;
import io.flybird.cubecraft.internal.renderer.HUDRenderer;

import io.flybird.cubecraft.internal.type.WorldType;
import io.flybird.cubecraft.internal.world.provider.WorldProviderOverWorld;
import io.flybird.cubecraft.internal.world.worldGen.WorldGeneratorOverworld;
import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.internal.ui.component.*;
import io.flybird.cubecraft.internal.ui.renderer.*;
import io.flybird.cubecraft.extansion.event.ModLoadEvent;
import io.flybird.cubecraft.internal.renderer.ChunkRenderer;

import io.flybird.cubecraft.world.biome.Biome;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.util.event.EventHandler;
import io.flybird.util.logging.LogHandler;

/**
 * internal content pack
 *
 * @author GrassBlock2022
 */
@CubecraftMod(name = "cubecraft-internal-content-pack", version = "")
public class InternalContent extends Mod {
    public final LogHandler logger = LogHandler.create("CubecraftInternal/ModMain");

    public void load(ModLoadEvent e) {
        if (e.client()) {
            logger.info("initialize in client side...");
            onGUIInit(null);
            ClientRegistries.WORLD_RENDERER.registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
            ClientRegistries.WORLD_RENDERER.registerItem("cubecraft:entity_renderer", EntityRenderer.class);
            ClientRegistries.WORLD_RENDERER.registerItem("cubecraft:hud_renderer", HUDRenderer.class);
        } else {
            logger.info("initialize in server side...");
        }
        this.onContentInitialize(null);
        logger.info("mod initialization complete.");
    }

    @EventHandler
    public void onContentInitialize(ContentInitializeEvent e){
        Registries.BLOCK_BEHAVIOR.registerFieldHolder(BlockBehaviorRegistry.class);
        Registries.BLOCK.registerFieldHolder(BlockRegistry.class);
        Registries.ENTITY.registerGetFunctionProvider(EntityRegistry.class);
        Registries.INVENTORY.registerGetFunctionProvider(InventoryRegistry.class);
        ClientRegistries.COLOR_MAP.registerGetter(ColorMapRegistry.class);
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


    @EventHandler
    public void onGUIInit(GUIInitializeEvent e){
        //component
        ClientRegistries.GUI_MANAGER.registerComponent("button", Button.class, new Button.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("image", ImageRenderer.class, new ImageRenderer.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("label", Label.class, new Label.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("waiting", WaitingAnimation.class, new WaitingAnimation.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("splash", SplashText.class, new SplashText.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("textbar", TextBar.class, new TextBar.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("topbar", TopBar.class, new TopBar.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("panel", Panel.class, new Panel.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerComponent("icon",Icon.class,new Icon.XMLDeserializer());

        //layout
        ClientRegistries.GUI_MANAGER.registerLayout("origin", OriginLayout.class, new OriginLayout.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerLayout("flow", FlowLayout.class, new FlowLayout.XMLDeserializer());
        ClientRegistries.GUI_MANAGER.registerLayout("viewport", ViewportLayout.class, new ViewportLayout.XMLDeserializer());

        //render_controller
        ClientRegistries.GUI_MANAGER.registerRenderController(Button.class, ResourceLocation.uiRenderController("cubecraft","button.json"));
        ClientRegistries.GUI_MANAGER.registerRenderController(Panel.class, ResourceLocation.uiRenderController("cubecraft","panel.json"));
        ClientRegistries.GUI_MANAGER.registerRenderController(TopBar.class, ResourceLocation.uiRenderController("cubecraft","topbar.json"));
        ClientRegistries.GUI_MANAGER.registerRenderController(TextBar.class,ResourceLocation.uiRenderController("cubecraft","textbar.json"));
        ClientRegistries.GUI_MANAGER.registerRenderController(WaitingAnimation.class, ResourceLocation.uiRenderController("cubecraft", "circlewaiting.json"));

        //renderer_part
        ClientRegistries.GUI_MANAGER.registerRendererPart("image_all_boarder", AllBoarderImage.class,new AllBoarderImage.JDeserializer());
        ClientRegistries.GUI_MANAGER.registerRendererPart("image_horizontal_boarder", HorizontalBoarderImage.class,new HorizontalBoarderImage.JDeserializer());
        ClientRegistries.GUI_MANAGER.registerRendererPart("image_vertical_boarder", VerticalBorderImage.class,new VerticalBorderImage.JDeserializer());
        ClientRegistries.GUI_MANAGER.registerRendererPart("font", Font.class,new Font.JDeserializer());
        ClientRegistries.GUI_MANAGER.registerRendererPart("image_animation", ImageAnimation.class,new ImageAnimation.JDeserializer());
        ClientRegistries.GUI_MANAGER.registerRendererPart("color", Color.class,new Color.JDeserializer());

        Registries.CLIENT.getWindow().getEventBus().registerEventListener(new ClientInputHandler(Registries.CLIENT));
        Registries.CLIENT.getClientEventBus().registerEventListener(new ScreenController());
    }


}
