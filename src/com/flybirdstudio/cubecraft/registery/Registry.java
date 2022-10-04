package com.flybirdstudio.cubecraft.registery;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.render.model.FaceRenderers;
import com.flybirdstudio.cubecraft.client.render.model.FaceTypeAdapterRenderer;
import com.flybirdstudio.cubecraft.client.render.model.ModelManager;
import com.flybirdstudio.cubecraft.client.render.model.object.BlockModel;
import com.flybirdstudio.cubecraft.client.render.model.object.EntityModel;
import com.flybirdstudio.cubecraft.client.render.model.object.Face;
import com.flybirdstudio.cubecraft.client.render.model.serilize.BlockModelSerializer;
import com.flybirdstudio.cubecraft.client.render.model.serilize.EntityModelSerializer;
import com.flybirdstudio.cubecraft.client.render.model.serilize.FaceSerializer;
import com.flybirdstudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.flybirdstudio.cubecraft.client.render.renderer.EntityRenderer;
import com.flybirdstudio.cubecraft.client.render.renderer.HUDRenderer;
import com.flybirdstudio.cubecraft.client.render.renderer.IWorldRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.flybirdstudio.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import com.flybirdstudio.cubecraft.net.NetWorkEventBus;
import com.flybirdstudio.cubecraft.registery.block.*;
import com.flybirdstudio.cubecraft.server.Server;
import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.biome.BiomeMap;
import com.flybirdstudio.cubecraft.world.block.material.Block;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.cubecraft.world.item.Item;
import com.flybirdstudio.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import com.flybirdstudio.cubecraft.world.worldGen.pipeline.object.ChunkGeneratorOverWorld;
import com.flybirdstudio.starfish3d.render.Camera;
import com.flybirdstudio.starfish3d.render.textures.TextureManager;
import com.flybirdstudio.util.container.namespace.NameSpacedConstructingMap;
import com.flybirdstudio.util.container.namespace.NameSpacedRegisterMap;
import com.flybirdstudio.util.net.PacketDecoder;
import com.flybirdstudio.util.net.PacketEncoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * simple register entry set...
 */
public class Registry {
    private Registry() {
        throw new RuntimeException("you should not create instance of this!");
    }

    private static Cubecraft client;
    private static Server server;

    public static void setClient(Cubecraft client) {
        Registry.client = client;
    }

    public static Cubecraft getClient() {
        return client;
    }

    public static void setServer(Server server) {
        Registry.server = server;
    }

    public static Server getServer() {
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
    private static final NameSpacedRegisterMap<? extends IBlockRenderer, ?> blockRendererMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<? extends IEntityRenderer, ?> entityRendererMap = new NameSpacedRegisterMap<>(null);
    public static NameSpacedConstructingMap<IWorldRenderer> worldRenderers = new NameSpacedConstructingMap<>(IWorld.class, Player.class, Camera.class);
    private static final ModelManager<BlockModel> blockModelManager = new ModelManager<>("/resource/fallback/block_model.json", BlockModel.class);
    private static final ModelManager<EntityModel> entityModelManager = new ModelManager<>("/resource/fallback/entity_model.json", EntityModel.class);
    private static final TextureManager textureManager = new TextureManager();
    public static NameSpacedRegisterMap<FaceTypeAdapterRenderer, ?> faceTypeAdapterRendererMap = new NameSpacedRegisterMap<>(null);

    public static ModelManager<BlockModel> getBlockModelManager() {
        return blockModelManager;
    }

    public static ModelManager<EntityModel> getEntityModelManager() {
        return entityModelManager;
    }

    public static NameSpacedRegisterMap<FaceTypeAdapterRenderer, ?> getFaceTypeAdapterRendererMap() {
        return faceTypeAdapterRendererMap;
    }

    public static TextureManager getTextureManager() {
        return textureManager;
    }

    public static NameSpacedRegisterMap<? extends IBlockRenderer, ?> getBlockRendererMap() {
        return blockRendererMap;
    }

    public static NameSpacedRegisterMap<? extends IEntityRenderer, ?> getEntityRendererMap() {
        return entityRendererMap;
    }

    public static NameSpacedConstructingMap<IWorldRenderer> getWorldRenderers() {
        return worldRenderers;
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
            .registerTypeAdapter(BlockModel.class, new BlockModelSerializer())
            .registerTypeAdapter(EntityModel.class, new EntityModelSerializer())
            .registerTypeAdapter(Face.class, new FaceSerializer()).create();

    public static Gson getJsonReader() {
        return gson;
    }


    //initialize
    public static void registerVanillaContent() {
        //block
        getBlockBehaviorMap().registerGetter(BlockBehaviorRegistery.class);
        getBlockMap().registerGetter(BlockRegistery.class);
        getBlockRendererMap().registerGetter(BlockRendererRegistry.class);

        getBlockMap().registerGetter(BlockRegistryTree.class);
        getBlockRendererMap().registerGetter(BlockRendererRegistryTree.class);

        //entity
        getBiomeMap().registerGetter(BiomesRegistry.class);
        getEntityRendererMap().registerGetter(EntityRendererRegistery.class);

        getWorldRenderers().registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        getWorldRenderers().registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        getWorldRenderers().registerItem("cubecraft:hud_renderer", HUDRenderer.class);

        getFaceTypeAdapterRendererMap().registerGetter(FaceRenderers.class);

        getWorldGeneratorMap().registerItem("cubecraft:overworld", new ChunkGeneratorPipeline()./*addLast(new BiomeBuilderOverWorld()).*/addLast(new ChunkGeneratorOverWorld()));
    }
}