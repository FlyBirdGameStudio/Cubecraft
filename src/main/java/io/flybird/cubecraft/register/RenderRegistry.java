package io.flybird.cubecraft.register;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.gui.renderer.ComponentRenderManager;
import io.flybird.cubecraft.client.render.model.ModelManager;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.IColorMap;
import io.flybird.cubecraft.client.render.model.object.EntityModel;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.textures.TextureManager;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

public class RenderRegistry {
    //render
    private static final NameSpacedRegisterMap<IBlockRenderer, ?> blockRendererMap = new NameSpacedRegisterMap<>(null);
    private static final NameSpacedRegisterMap<IEntityRenderer, ?> entityRendererMap = new NameSpacedRegisterMap<>(null);
    private static final ModelManager<BlockModel> blockModelManager = new ModelManager<>(BlockModel.class);
    private static final ModelManager<EntityModel> entityModelManager = new ModelManager<>(EntityModel.class);
    private static final TextureManager textureManager = new TextureManager();
    private static final NameSpacedRegisterMap<IColorMap, ?> colorMaps = new NameSpacedRegisterMap<>(null);
    private static final ComponentRenderManager componentRenderManager = new ComponentRenderManager();
    public static NameSpacedConstructingMap<IWorldRenderer> worldRenderers = new NameSpacedConstructingMap<>(IWorld.class, Player.class, Camera.class, GameSetting.class);

    private RenderRegistry() {
        throw new RuntimeException("you should not create instance of this!");
    }

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

    public static ComponentRenderManager getComponentRenderManager() {
        return componentRenderManager;
    }
}
