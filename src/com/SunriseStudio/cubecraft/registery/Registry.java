package com.sunrisestudio.cubecraft.registery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunrisestudio.cubecraft.client.render.model.ModelManager;
import com.sunrisestudio.cubecraft.client.render.model.object.BlockModel;
import com.sunrisestudio.cubecraft.client.render.model.object.EntityModel;
import com.sunrisestudio.cubecraft.client.render.model.object.Face;
import com.sunrisestudio.cubecraft.client.render.model.serilize.BlockModelSerializer;
import com.sunrisestudio.cubecraft.client.render.model.serilize.EntityModelSerializer;
import com.sunrisestudio.cubecraft.client.render.model.serilize.FaceSerializer;
import com.sunrisestudio.cubecraft.client.render.renderer.ChunkRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.EntityRenderer;
import com.sunrisestudio.cubecraft.client.render.renderer.IWorldRenderer;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.BlockRenderers;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.EntityRenderers;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import com.sunrisestudio.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.material.Block;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.grass3d.render.textures.TextureManager;
import com.sunrisestudio.util.container.namespace.NameSpacedConstructingMap;
import com.sunrisestudio.util.container.namespace.NameSpacedRegisterMap;

/**
 * simple register entry set...
 */
public class Registry {
    //game content
    private static final NameSpacedRegisterMap<Block,?> blockBehaviorMap=new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<Block, Block> blockMap=new NameSpacedRegisterMap<>(blockBehaviorMap);
    private static final NameSpacedConstructingMap<Entity> entityMap=new NameSpacedConstructingMap<>(IWorldAccess.class);

    public static NameSpacedRegisterMap<Block, ?> getBlockBehaviorMap() {
        return blockBehaviorMap;
    }
    public static NameSpacedRegisterMap<Block, Block> getBlockMap() {
        return blockMap;
    }
    public static NameSpacedConstructingMap<Entity> getEntityMap() {
        return entityMap;
    }


    //render
    private static final ModelManager<BlockModel> blockModelManager= new ModelManager<>("/resource/fallback/block_model.json", BlockModel.class);
    private static final ModelManager<EntityModel> entityModelManager=new ModelManager<>("/resource/fallback/entity_model.json",EntityModel.class);
    private static final TextureManager textureManager=new TextureManager();
    private static final NameSpacedRegisterMap<? extends IBlockRenderer,?> blockRendererMap=new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<? extends IEntityRenderer,?> entityRendererMap=new NameSpacedRegisterMap<>(null);
    public static NameSpacedConstructingMap<IWorldRenderer> worldRenderers=new NameSpacedConstructingMap<>(IWorldAccess.class,Player.class, Camera.class);

    public static ModelManager<BlockModel> getBlockModelManager() {
        return blockModelManager;
    }
    public static ModelManager<EntityModel> getEntityModelManager() {
        return entityModelManager;
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
    public static NameSpacedConstructingMap<IWorldRenderer> getWorldRenderers() {return worldRenderers;}

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
        blockBehaviorMap.registerGetter(BlockBehaviors.class);
        blockMap.registerGetter(Blocks.class);

        //entity
        getBlockRendererMap().registerGetter(BlockRenderers.class);
        getEntityRendererMap().registerGetter(EntityRenderers.class);

        getWorldRenderers().registerItem("cubecraft:chunk_renderer", ChunkRenderer.class);
        getWorldRenderers().registerItem("cubecraft:entity_renderer", EntityRenderer.class);
        //getWorldRenderers().registerItem("cubecraft:environment_renderer", EnvironmentRenderer.class);
    }
}