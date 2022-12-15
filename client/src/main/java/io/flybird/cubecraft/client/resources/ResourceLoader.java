package io.flybird.cubecraft.client.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.flybird.cubecraft.client.ClientMain;
import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.event.ClientResourceReloadEvent;
import io.flybird.cubecraft.client.gui.font.LegacyFontRenderer;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.BlockModelComponent;
import io.flybird.cubecraft.client.render.model.block.BlockModelFace;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.register.Registries;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.starfish3d.render.textures.Texture2DTileMap;
import io.flybird.starfish3d.render.textures.TextureStateManager;
import io.flybird.util.I18nHelper;
import io.flybird.util.container.namespace.NameSpaceMap;
import io.flybird.util.event.EventListener;
import io.flybird.util.logging.LogHandler;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

public class ResourceLoader implements EventListener {
    public final ArrayList<ResourceLocation> textureList = new ArrayList<>();
    final LogHandler logger = LogHandler.create("Client/ResourceLoader");

    @ResourceLoadHandler(stage = ResourceLoadStage.BLOCK_MODEL)
    public void loadBlockModel(ClientResourceReloadEvent e) {
        Registries.GSON_BUILDER.registerTypeAdapter(BlockModel.class, new BlockModel.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(BlockModelFace.class, new BlockModelFace.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(BlockModelComponent.class, new BlockModelComponent.JDeserializer());

        try {
            for (IBlockRenderer renderer : ((NameSpaceMap<? extends IBlockRenderer>) ClientRegistries.BLOCK_RENDERER).itemList()) {
                if (renderer != null) {
                    renderer.initializeRenderer(textureList);
                }
            }
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.BLOCK_TEXTURE)
    public void loadBlockTexture(ClientResourceReloadEvent e) {
        try {
            Resource[] f = new Resource[textureList.size()];
            int i = 0;
            for (ResourceLocation loc : textureList) {
                f[i] = ClientRegistries.RESOURCE_MANAGER.getResource(loc);
                i++;
            }
            Texture2DTileMap terrain = ClientRegistries.TEXTURE.getTexture2DTileMapContainer().set("cubecraft:terrain", Texture2DTileMap.autoGenerate(f, false));
            for (Resource r : f) {
                terrain.register(r);
            }

            terrain.generateTexture();
            terrain.completePlannedLoad(e.client(), 0, 100);
            terrain.upload();

            if (ClientMain.getStartGameArguments().getValueAsBoolean("debug", false)) {
                File f2 = new File(ClientMain.getGamePath() + "/data/debug/terrain.png");
                terrain.export(f2);
            }


            TextureStateManager.setTextureMipMap(terrain, true);
            TextureStateManager.setTextureClamp(terrain, true);
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.COLOR_MAP)
    public void loadColorMap(ClientResourceReloadEvent e) {
        try {
            Collection<String> s = ClientRegistries.COLOR_MAP.idList();
            for (String s2 : s) {
                ClientRegistries.COLOR_MAP.get(s2).load();
            }
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.LANGUAGE)
    public void loadLanguage(ClientResourceReloadEvent e) {
        String f = (String) Registries.CLIENT.getGameSetting().getValue("client.language", "auto");
        if (Objects.equals(f, "auto")) {
            Locale locale = Locale.getDefault();
            String lang = locale.getLanguage();
            String reg = locale.getCountry();
            f = I18nHelper.covert(locale);
            logger.info("detected user language:%s-%s", lang, reg);
        }
        Registries.I18N.setCurrentType(f);
        try {
            for (String s : e.resourceManager().getNameSpaces()) {
                JsonArray arr = JsonParser.parseString(e.resourceManager().getResource(ResourceLocation.language(s, "language_index.json")).getAsText()).getAsJsonArray();
                for (JsonElement registry : arr) {
                    String type = registry.getAsJsonObject().get("type").getAsString();
                    String author = registry.getAsJsonObject().get("author").getAsString();
                    String display = registry.getAsJsonObject().get("author").getAsString();
                    String file = registry.getAsJsonObject().get("file").getAsString();

                    String data = e.resourceManager().getResource(ResourceLocation.language(file.split(":")[0], file.split(":")[1])).getAsText();

                    if (!Registries.I18N.has(type)) {
                        Registries.I18N.createNew(type, display);
                    }
                    Registries.I18N.attach(type, data);
                    Registries.I18N.addAuthor(type, author);
                }
            }
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.FONT_TEXTURE)
    public void loadFontTexture(ClientResourceReloadEvent e) {
        ClientRegistries.ICON_FONT_RENDERER.setFontFamily(e.resourceManager().getResource(ResourceLocation.font("cubecraft", "SegMDL2.ttf")).getAsFont());
        ClientRegistries.SMOOTH_FONT_RENDERER.setFontFamily(e.resourceManager().getResource(ResourceLocation.font("cubecraft", "OPPOSans.ttf")).getAsFont().deriveFont(Font.BOLD));

        if (ClientRegistries.GUI_MANAGER.isLegacyFontRequested()) {
            try {
                long last = System.currentTimeMillis();
                for (int i = 0; i < 256; i++) {
                    if (i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8 || i == 0xf0) {
                        continue;
                    }
                    String s2 = Integer.toHexString(i);
                    if (s2.length() == 1) {
                        s2 = "0" + Integer.toHexString(i);
                    }
                    LegacyFontRenderer.textures[i] = new Texture2D(false, true);
                    LegacyFontRenderer.textures[i].generateTexture();
                    LegacyFontRenderer.textures[i].load(ClientRegistries.RESOURCE_MANAGER.getResource("/resource/cubecraft/texture/font/unicode_page_%s.png".formatted(s2)));
                    TextureStateManager.setTextureMipMap(LegacyFontRenderer.textures[i], true);
                    if (System.currentTimeMillis() - last > 16) {
                        System.gc();
                        last = System.currentTimeMillis();
                        e.client().onProgressChange((int) ((float) i / 256 * 100));
                        e.client().onProgressStageChanged("loadingFontTexture:/resource/texture/font/unicode_page_%s.png(%d/256)".formatted(s2, i));
                        e.client().refreshScreen();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                this.logger.exception(ex);
            }
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.UI_CONTROLLER)
    public void loadUIController(ClientResourceReloadEvent e) {
        ClientRegistries.GUI_MANAGER.initializeUIRenderController();
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.DETECT)
    public void detectResourceLoader(ClientResourceReloadEvent e) {
        e.resourceManager().addNameSpace("cubecraft");
    }
}
