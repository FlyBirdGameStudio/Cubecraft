package com.sunrisestudio.cubecraft.registery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunrisestudio.cubecraft.client.render.model.FaceRenderers;
import com.sunrisestudio.cubecraft.client.render.model.FaceTypeAdapterRenderer;
import com.sunrisestudio.cubecraft.client.render.model.ModelManager;
import com.sunrisestudio.cubecraft.client.render.model.object.BlockModel;
import com.sunrisestudio.cubecraft.client.render.model.object.EntityModel;
import com.sunrisestudio.cubecraft.client.render.model.object.Face;
import com.sunrisestudio.cubecraft.client.render.model.serilize.BlockModelSerializer;
import com.sunrisestudio.cubecraft.client.render.model.serilize.EntityModelSerializer;
import com.sunrisestudio.cubecraft.client.render.model.serilize.FaceSerializer;
import com.sunrisestudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.EntityRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.HUDRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.IWorldRenderer;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.biome.BiomeMap;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.world.item.Item;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.object.ChunkGeneratorOverWorld;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.grass3d.render.textures.TextureManager;
import com.sunrisestudio.util.container.namespace.NameSpacedConstructingMap;
import com.sunrisestudio.util.container.namespace.NameSpacedRegisterMap;
import com.sunrisestudio.util.file.lang.Language;
import com.sunrisestudio.util.net.PacketDecoder;
import com.sunrisestudio.util.net.PacketEncoder;

/**
 * simple register entry set...
 */
public class Registry {
    //game content
    private static final NameSpacedRegisterMap<Block,?> blockBehaviorMap=new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<Block, Block> blockMap=new NameSpacedRegisterMap<>(blockBehaviorMap);
    private static final NameSpacedConstructingMap<Entity> entityMap=new NameSpacedConstructingMap<>(World.class);
    private static final NameSpacedRegisterMap<ChunkGeneratorPipeline,?> worldGeneratorMap=new NameSpacedRegisterMap<>(null);
    private static final BiomeMap biomeMap=new BiomeMap();
    private static final NameSpacedRegisterMap<Item,?> itemMap=new NameSpacedRegisterMap<>(null);

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

    //model&texture
    private static final ModelManager<BlockModel> blockModelManager= new ModelManager<>("/resource/fallback/block_model.json", BlockModel.class);
    private static final ModelManager<EntityModel> entityModelManager=new ModelManager<>("/resource/fallback/entity_model.json",EntityModel.class);
    private static final TextureManager textureManager=new TextureManager();
    public static NameSpacedRegisterMap<FaceTypeAdapterRenderer,?>faceTypeAdapterRendererMap=new NameSpacedRegisterMap<>(null);

    public static ModelManager<BlockModel> getBlockModelManager() {
        return blockModelManager;
    }
    public static ModelManager<EntityModel> getEntityModelManager() {
        return entityModelManager;
    }
    public static NameSpacedRegisterMap<FaceTypeAdapterRenderer, ?> getFaceTypeAdapterRendererMap() {return faceTypeAdapterRendererMap;}
    public static TextureManager getTextureManager() {
        return textureManager;
    }


    //render
    private static final NameSpacedRegisterMap<? extends IBlockRenderer,?> blockRendererMap=new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<? extends IEntityRenderer,?> entityRendererMap=new NameSpacedRegisterMap<>(null);
    public static NameSpacedConstructingMap<IWorldRenderer> worldRenderers=new NameSpacedConstructingMap<>(World.class,Player.class, Camera.class);

    public static NameSpacedRegisterMap<? extends IBlockRenderer, ?> getBlockRendererMap() {
        return blockRendererMap;
    }
    public static NameSpacedRegisterMap<? extends IEntityRenderer, ?> getEntityRendererMap() {
        return entityRendererMap;
    }
    public static NameSpacedConstructingMap<IWorldRenderer> getWorldRenderers() {return worldRenderers;}


    //net
    private static final NameSpacedRegisterMap<? extends PacketEncoder,?> packetEncoderMap=new NameSpacedRegisterMap(null);
    private static final NameSpacedRegisterMap<? extends PacketDecoder,?> packetDecoderMap=new NameSpacedRegisterMap(null);

    public static NameSpacedRegisterMap<? extends PacketDecoder, ?> getPacketDecoderMap() {
        return packetDecoderMap;
    }
    public static NameSpacedRegisterMap<? extends PacketEncoder, ?> getPacketEncoderMap() {
        return packetEncoderMap;
    }

    //reader
    private static final Gson gson=new GsonBuilder()
            .registerTypeAdapter(BlockModel.class,new BlockModelSerializer())
            .registerTypeAdapter(EntityModel.class,new EntityModelSerializer())
            .registerTypeAdapter(Face.class,new FaceSerializer()).create();
    public static Gson getJsonReader() {
        return gson;
    }


    //initialize
    public static void registerVanillaContent(){
        //block
        getBlockBehaviorMap().registerGetter(BlockBehaviorRegistery.class);
        getBlockMap().registerGetter(BlockRegistery.class);
        getBiomeMap().registerGetter(BiomesRegistry.class);


        //entity
        getBlockRendererMap().registerGetter(BlockRendererRegistery.class);
        getEntityRendererMap().registerGetter(EntityRendererRegistery.class);

        getWorldRenderers().registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        getWorldRenderers().registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        getWorldRenderers().registerItem("cubecraft:hud_renderer", HUDRenderer.class);

        getFaceTypeAdapterRendererMap().registerGetter(FaceRenderers.class);

        getWorldGeneratorMap().registerItem("cubecraft:overworld", new ChunkGeneratorPipeline()./*addLast(new BiomeBuilderOverWorld()).*/addLast(new ChunkGeneratorOverWorld()));


        registerLanguage();
    }

    public static void registerLanguage(){
        Language.create(Language.LanguageType.ZH_CN);
        Language.selectInstance(Language.LanguageType.ZH_CN).attachTranslationFile(ResourceManager.instance.getResource(
                "/resource/text/language/zh_cn.lang",
                "/resource/text/language/blank.lang"
        ));
        Language.create(Language.LanguageType.EN_US);
        Language.selectInstance(Language.LanguageType.EN_US).attachTranslationFile(ResourceManager.instance.getResource(
                "/resource/text/language/en_us.lang",
                "/resource/text/language/blank.lang"
        ));
    }


}