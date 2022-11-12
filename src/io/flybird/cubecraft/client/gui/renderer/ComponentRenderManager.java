package io.flybird.cubecraft.client.gui.renderer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.gui.component.Panel;
import io.flybird.cubecraft.client.gui.component.TopBar;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.component.control.TextBar;
import io.flybird.cubecraft.client.gui.renderer.comp.*;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.util.container.CollectionUtil;

import java.util.*;

public class ComponentRenderManager {
    private final HashMap<Class<? extends Node>,ComponentRenderer> renderers=new HashMap<>();

    private final Gson gson=new GsonBuilder()
            .registerTypeAdapter(ComponentRenderer.class,new ComponentRenderer.JDeserializer())

            .registerTypeAdapter(ComponentPartRenderer.class,new ComponentPartRenderer.JDeserializer())
            .registerTypeAdapter(HorizontalBoarderImage.class,new HorizontalBoarderImage.JDeserializer())
            .registerTypeAdapter(VerticalBorderImage.class,new ComponentPartRenderer.JDeserializer())
            .registerTypeAdapter(AllBoarderImage.class,new AllBoarderImage.JDeserializer())
            .registerTypeAdapter(Font.class,new Font.JDeserializer())
            .registerTypeAdapter(Color.class,new Color.JDeserializer())

            .create();


    public void reload(){
        this.renderers.put(Button.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft","button.json")).getAsText(),ComponentRenderer.class));
        this.renderers.put(Panel.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft","panel.json")).getAsText(),ComponentRenderer.class));
        this.renderers.put(TopBar.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft","topbar.json")).getAsText(),ComponentRenderer.class));
        this.renderers.put(TextBar.class, gson.fromJson(ResourceManager.instance.getResource(ResourceLocation.uiRenderController("cubecraft","textbar.json")).getAsText(),ComponentRenderer.class));
    }

    public void init(){
        List<ResourceLocation> locations=new ArrayList<>();
        CollectionUtil.iterateMap(this.renderers,((key, item) -> item.initializeModel(locations)));
        for (ResourceLocation loc:locations){
            Registry.getTextureManager().createTexture2D(ResourceManager.instance.getResource(loc),false,false);
        }
    }

    public ComponentRenderer get(Class<? extends Node> clazz){
        return this.renderers.get(clazz);
    }

}
