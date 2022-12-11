package io.flybird.cubecraft.register;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.auth.SessionService;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.client.gui.renderer.ComponentRenderManager;
import io.flybird.cubecraft.client.render.model.ModelManager;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.IColorMap;
import io.flybird.cubecraft.client.render.model.object.EntityModel;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.IWorldProvider;
import io.flybird.cubecraft.world.biome.BiomeMap;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.item.Item;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.starfish3d.platform.Window;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.textures.TextureManager;
import io.flybird.util.file.XmlReader;
import io.flybird.util.I18nHelper;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.util.DebugInfoHandler;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

/**
 * simple register entry set...
 */
public class Registries {
    public static final DebugInfoHandler DEBUG_INFO = new DebugInfoHandler();
    public static final GsonBuilder GSON_BUILDER=new GsonBuilder();
    public static final XmlReader FAML_READER = new XmlReader();

    //common
    public static final NameSpacedRegisterMap<SessionService, ?> SESSION_SERVICE = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedConstructingMap<Packet> PACKET = new NameSpacedConstructingMap<>();
    public static final I18nHelper I18N =new I18nHelper();
    public static final ScreenLoader SCREEN_LOADER = new ScreenLoader();

    public static Cubecraft CLIENT;
    public static CubecraftServer SERVER;

    //render
    public static final NameSpacedRegisterMap<IBlockRenderer, ?> BLOCK_RENDERER = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<IEntityRenderer, ?> ENTITY_RENDERER = new NameSpacedRegisterMap<>(null);
    public static final ModelManager<BlockModel> BLOCK_MODEL = new ModelManager<>(BlockModel.class);
    public static final ModelManager<EntityModel> ENTITY_MODEL = new ModelManager<>(EntityModel.class);
    public static final TextureManager TEXTURE = new TextureManager();
    public static final NameSpacedRegisterMap<IColorMap, ?> COLOR_MAP = new NameSpacedRegisterMap<>(null);
    public static final ComponentRenderManager COMPONENT_RENDERER = new ComponentRenderManager();
    public static final NameSpacedConstructingMap<IWorldRenderer> WORLD_RENDERER = new NameSpacedConstructingMap<>(Window.class, IWorld.class, Player.class, Camera.class, GameSetting.class);

    //content
    public static final NameSpacedRegisterMap<Block, ?> BLOCK_BEHAVIOR = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<Block, Block> BLOCK = new NameSpacedRegisterMap<>(BLOCK_BEHAVIOR);
    public static final NameSpacedConstructingMap<Entity> ENTITY = new NameSpacedConstructingMap<>(IWorld.class);
    public static final NameSpacedRegisterMap<ChunkGeneratorPipeline, ?> WORLD_GENERATOR = new NameSpacedRegisterMap<>(null);
    public static final BiomeMap BIOME = new BiomeMap();
    public static final NameSpacedRegisterMap<Item, ?> ITEM = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<IWorldProvider,?> WORLD_PROVIDER =new NameSpacedRegisterMap<>(null);




    private Registries() {
        throw new RuntimeException("you should not create instance of this!");
    }

    public static Gson createJsonReader(){
        return GSON_BUILDER.create();
    }
}