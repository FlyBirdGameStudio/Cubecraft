package io.flybird.cubecraft.register;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.renderer.ComponentRenderManager;
import io.flybird.cubecraft.client.render.model.ModelManager;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.BlockModelFace;
import io.flybird.cubecraft.client.render.model.block.IBlockModelComponent;
import io.flybird.cubecraft.client.render.model.block.IColorMap;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelCompDeserializer;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelDeserializer;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelFaceDeserializer;
import io.flybird.cubecraft.client.render.model.object.EntityModel;
import io.flybird.cubecraft.client.render.renderer.ChunkRenderer;
import io.flybird.cubecraft.client.render.renderer.EntityRenderer;
import io.flybird.cubecraft.client.render.renderer.HUDRenderer;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import io.flybird.cubecraft.internal.worldGen.WorldGeneratorOverworld;
import io.flybird.cubecraft.net.NetWorkEventBus;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.biome.BiomeMap;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.item.Item;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.cubecraft.internal.worldGen.ChunkGeneratorOverWorld;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.textures.TextureManager;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;
import io.flybird.util.net.PacketDecoder;
import io.flybird.util.net.PacketEncoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * simple register entry set...
 */
public class Registry {
    private static ArrayList<String> worldIdList=new ArrayList<>();

    private Registry() {
        throw new RuntimeException("you should not create instance of this!");
    }

    private static Cubecraft client;
    private static CubecraftServer server;

    public static void setClient(Cubecraft client) {
        Registry.client = client;
    }

    public static Cubecraft getClient() {
        return client;
    }

    public static void setServer(CubecraftServer server) {
        Registry.server = server;
    }

    public static CubecraftServer getServer() {
        return server;
    }

    //game content
    private static final NameSpacedRegisterMap<Block, ?> blockBehaviorMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<Block, Block> blockMap = new NameSpacedRegisterMap<>(blockBehaviorMap);
    private static final NameSpacedConstructingMap<Entity> entityMap = new NameSpacedConstructingMap<>(IWorld.class);
    private static final NameSpacedRegisterMap<ChunkGeneratorPipeline, ?> worldGeneratorMap = new NameSpacedRegisterMap<>(null);
    private static final BiomeMap biomeMap = new BiomeMap();
    private static final NameSpacedRegisterMap<Item, ?> itemMap = new NameSpacedRegisterMap<>(null);

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

    //render
    private static final NameSpacedRegisterMap<IBlockRenderer, ?> blockRendererMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<IEntityRenderer, ?> entityRendererMap = new NameSpacedRegisterMap<>(null);
    public static NameSpacedConstructingMap<IWorldRenderer> worldRenderers = new NameSpacedConstructingMap<>(IWorld.class, Player.class, Camera.class);
    private static final ModelManager<BlockModel> blockModelManager = new ModelManager<>(BlockModel.class);
    private static final ModelManager<EntityModel> entityModelManager = new ModelManager<>(EntityModel.class);
    private static final TextureManager textureManager = new TextureManager();
    private static final NameSpacedRegisterMap<IColorMap,?>colorMaps=new NameSpacedRegisterMap<>(null);

    public static NameSpacedRegisterMap<IColorMap, ?> getColorMaps() {
        return colorMaps;
    }

    public static ModelManager<BlockModel> getBlockModelManager() {
        return blockModelManager;
    }

    public static ModelManager<EntityModel> getEntityModelManager() {
        return entityModelManager;
    }

    public static TextureManager getTextureManager() {
        return textureManager;
    }

    public static NameSpacedRegisterMap<IBlockRenderer, ?> getBlockRendererMap() {
        return blockRendererMap;
    }

    public static NameSpacedRegisterMap<IEntityRenderer, ?> getEntityRendererMap() {
        return entityRendererMap;
    }

    public static NameSpacedConstructingMap<IWorldRenderer> getWorldRenderers() {
        return worldRenderers;
    }


    private static final ComponentRenderManager componentRenderManager =new ComponentRenderManager();

    public static ComponentRenderManager getComponentRenderManager() {
        return componentRenderManager;
    }

    //network
    private static final NameSpacedRegisterMap<? extends PacketEncoder<?>, ?> packetEncoderMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<? extends PacketDecoder<?>, ?> packetDecoderMap = new NameSpacedRegisterMap<>(null);
    private static final NetWorkEventBus networkEventBus = new NetWorkEventBus();

    public static NameSpacedRegisterMap<? extends PacketDecoder<?>, ?> getPacketDecoderMap() {
        return packetDecoderMap;
    }

    public static NameSpacedRegisterMap<? extends PacketEncoder<?>, ?> getPacketEncoderMap() {
        return packetEncoderMap;
    }

    public static NetWorkEventBus getNetworkEventBus() {
        return networkEventBus;
    }

    //reader
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(BlockModel .class, new BlockModelDeserializer())
            .registerTypeAdapter(BlockModelFace.class, new BlockModelFaceDeserializer())
            .registerTypeAdapter(IBlockModelComponent.class,new BlockModelCompDeserializer())
            .create();

    public static Gson getJsonReader() {
        return gson;
    }


    //initialize
    public static void registerVanillaContent() {
        getWorldRenderers().registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        getWorldRenderers().registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        getWorldRenderers().registerItem("cubecraft:hud_renderer", HUDRenderer.class);

    }

    public static ArrayList<String> getWorldIdList() {
        return worldIdList;
    }
}