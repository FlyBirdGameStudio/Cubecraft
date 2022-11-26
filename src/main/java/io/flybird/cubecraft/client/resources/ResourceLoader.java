package io.flybird.cubecraft.client.resources;

import io.flybird.cubecraft.client.event.ClientResourceReloadEvent;
import io.flybird.cubecraft.client.gui.FontRenderer;
import io.flybird.cubecraft.client.render.worldObjectRenderer.IBlockRenderer;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.register.RenderRegistry;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.starfish3d.render.textures.Texture2DTileMap;
import io.flybird.starfish3d.render.textures.TextureStateManager;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.container.namespace.NameSpaceMap;
import io.flybird.util.event.EventListener;
import io.flybird.util.file.lang.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceLoader implements EventListener {
    LogHandler logger = LogHandler.create("Client/ResourceLoader");

    public ArrayList<ResourceLocation> textureList = new ArrayList<>();

    @ResourceLoadHandler(stage = ResourceLoadStage.BLOCK_MODEL)
    public void loadBlockModel(ClientResourceReloadEvent e) {
        try {
            for (IBlockRenderer renderer : ((NameSpaceMap<? extends IBlockRenderer>) RenderRegistry.getBlockRendererMap()).itemList()) {
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
                f[i] = ResourceManager.instance.getResource(loc);
                i++;
            }
            Texture2DTileMap terrain = RenderRegistry.getTextureManager().getTexture2DTileMapContainer().set("cubecraft:terrain", Texture2DTileMap.autoGenerate(f, false));
            for (Resource r : f) {
                terrain.register(r);
            }

            terrain.generateTexture();
            terrain.completePlannedLoad(e.client(), 0, 100);
            File f2=new File("E:/terrain.png");
            f2.createNewFile();
            terrain.upload();
            terrain.export(f2);
            TextureStateManager.setTextureMipMap(terrain, true);
            TextureStateManager.setTextureClamp(terrain, true);
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.COLOR_MAP)
    public void loadColorMap(ClientResourceReloadEvent e) {
        try {
            Collection<String> s = RenderRegistry.getColorMaps().idList();
            for (String s2 : s) {
                RenderRegistry.getColorMaps().get(s2).load();
            }
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.LANGUAGE)
    public void loadLanguage(ClientResourceReloadEvent e) {
        try {
            HashMap<Language.LanguageType, String> languages = new HashMap<>();
            languages.put(Language.LanguageType.ZH_CN, "/resource/cubecraft/text/language/zh_cn.lang");
            languages.put(Language.LanguageType.EN_US, "/resource/cubecraft/text/language/en_us.lang");

            AtomicLong last = new AtomicLong(System.currentTimeMillis());
            int i = 0;
            CollectionUtil.iterateMap(languages, (key, item) -> {
                if (System.currentTimeMillis() - last.get() > 16) {
                    last.set(System.currentTimeMillis());
                    e.client().onProgressStageChanged("loading language file:zh_cn(%d/%d)".formatted(i, languages.size()));
                    e.client().refreshScreen();
                    e.client().onProgressChange((int) ((float) i / languages.size() * 100));
                }

                Language.create(key);
                Language.selectInstance(key).attachTranslationFile(ResourceManager.instance.getResource(item).getAsText());
            });
            Language.selectInstance(Language.LanguageType.valueOf((String) Registry.getClient().getGameSetting().getValue("client.language", "ZH_CN")));
            System.gc();
        } catch (Exception ex) {
            this.logger.exception(ex);
        }
    }

    @ResourceLoadHandler(stage = ResourceLoadStage.FONT_TEXTURE)
    public void loadFontTexture(ClientResourceReloadEvent e) {
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
                FontRenderer.textures[i] = new Texture2D(false, true);
                FontRenderer.textures[i].generateTexture();
                FontRenderer.textures[i].load(ResourceManager.instance.getResource("/resource/cubecraft/texture/font/unicode_page_%s.png".formatted(s2)));
                TextureStateManager.setTextureMipMap(FontRenderer.textures[i], true);
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

    @ResourceLoadHandler(stage = ResourceLoadStage.UI_CONTROLLER)
    public void loadUIController(ClientResourceReloadEvent e){
        RenderRegistry.getComponentRenderManager().reload();
        RenderRegistry.getComponentRenderManager().init();
    }
}
