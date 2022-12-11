package io.flybird.cubecraft.client.gui.renderer;

import com.google.gson.Gson;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.internal.ui.component.CircleWaitingAnimation;
import io.flybird.cubecraft.internal.ui.component.Panel;
import io.flybird.cubecraft.internal.ui.component.TopBar;
import io.flybird.cubecraft.internal.ui.component.Button;
import io.flybird.cubecraft.internal.ui.component.TextBar;
import io.flybird.cubecraft.client.gui.renderer.comp.*;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.container.CollectionUtil;

import java.util.*;

public class ComponentRenderManager {
    private final HashMap<Class<? extends Node>, ComponentRenderer> renderers = new HashMap<>();
    public void reload() {
        Registries.GSON_BUILDER.registerTypeAdapter(ComponentRenderer.class, new ComponentRenderer.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(ComponentPartRenderer.class, new ComponentPartRenderer.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(HorizontalBoarderImage.class, new HorizontalBoarderImage.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(VerticalBorderImage.class, new ComponentPartRenderer.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(AllBoarderImage.class, new AllBoarderImage.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(Font.class, new Font.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(Color.class, new Color.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(ImageAnimation.class, new ImageAnimation.JDeserializer());

        Gson gson = Registries.createJsonReader();

        this.renderers.put(Button.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft", "button.json")).getAsText(), ComponentRenderer.class));
        this.renderers.put(Panel.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft", "panel.json")).getAsText(), ComponentRenderer.class));
        this.renderers.put(TopBar.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft", "topbar.json")).getAsText(), ComponentRenderer.class));
        this.renderers.put(TextBar.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft", "textbar.json")).getAsText(), ComponentRenderer.class));
        this.renderers.put(CircleWaitingAnimation.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft", "circle_waiting_animation.json")).getAsText(), ComponentRenderer.class));
    }

    public void init() {
        List<ResourceLocation> locations = new ArrayList<>();
        CollectionUtil.iterateMap(this.renderers, ((key, item) -> item.initializeModel(locations)));
        for (ResourceLocation loc : locations) {
            Registries.TEXTURE.createTexture2D(ResourceManager.instance.getResource(loc), false, false);
        }
    }

    public ComponentRenderer get(Class<? extends Node> clazz) {
        return this.renderers.get(clazz);
    }
}
