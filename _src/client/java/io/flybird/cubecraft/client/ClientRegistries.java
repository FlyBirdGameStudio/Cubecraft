package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.gui.GUIManager;
import io.flybird.cubecraft.client.gui.font.SmoothedFontRenderer;
import io.flybird.cubecraft.client.network.ClientNetHandler;
import io.flybird.cubecraft.client.render.model.ModelManager;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.IColorMap;
import io.flybird.cubecraft.client.render.model.object.EntityModel;
import io.flybird.cubecraft.client.render.renderer.IWorldRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IEntityRenderer;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.living.Player;
import io.flybird.quantum3d.platform.Window;
import io.flybird.quantum3d.Camera;
import io.flybird.quantum3d.textures.TextureManager;
import io.flybird.util.GameSetting;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

public class ClientRegistries {
    public static final GUIManager GUI_MANAGER = new GUIManager();
    //render
    public static final NameSpacedRegisterMap<IBlockRenderer, ?> BLOCK_RENDERER = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<IEntityRenderer, ?> ENTITY_RENDERER = new NameSpacedRegisterMap<>(null);
    public static final ModelManager<BlockModel> BLOCK_MODEL = new ModelManager<>(BlockModel.class, ResourceLocation.blockModel("cubecraft","fallback.json"));
    public static final ModelManager<EntityModel> ENTITY_MODEL = new ModelManager<>(EntityModel.class,null);//todo:fallback
    public static final TextureManager TEXTURE = new TextureManager();
    public static final NameSpacedRegisterMap<IColorMap, ?> COLOR_MAP = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedConstructingMap<IWorldRenderer> WORLD_RENDERER = new NameSpacedConstructingMap<>(Window.class, IWorld.class, Player.class, Camera.class, GameSetting.class);
    public static final SmoothedFontRenderer SMOOTH_FONT_RENDERER = new SmoothedFontRenderer();
    public static final SmoothedFontRenderer ICON_FONT_RENDERER = new SmoothedFontRenderer();
    public static final ResourceManager RESOURCE_MANAGER =new ResourceManager();

    public static final NameSpacedConstructingMap<ClientNetHandler> NET_HANDLER=new NameSpacedConstructingMap<>();
}
