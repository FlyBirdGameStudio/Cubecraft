package io.flybird.cubecraft.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import io.flybird.cubecraft.client.gui.base.ComponentPartRenderer;
import io.flybird.cubecraft.client.gui.base.ComponentRenderer;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.cubecraft.client.gui.component.Node;
import io.flybird.cubecraft.client.gui.component.Screen;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.file.FAMLDeserializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GUIManager {
    private final HashMap<String, Class<? extends Node>> componentClassMapping = new HashMap<>();
    private final HashMap<String, Class<? extends LayoutManager>> layoutClassMapping = new HashMap<>();
    private final HashMap<String, Class<? extends ComponentPartRenderer>> rendererPartClassMapping = new HashMap<>();
    private final HashMap<Class<? extends Node>, ComponentRenderer> renderers = new HashMap<>();
    private final HashMap<Class<? extends Node>,ResourceLocation> renderControllerLocations =new HashMap<>();

    public GUIManager(){
        //basic
        Registries.FAML_READER.registerDeserializer(Text.class, new Text.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(LayoutManager.class, new LayoutManager.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(Screen.class, new Screen.XMLDeserializer());

        //layout
        Registries.FAML_READER.registerDeserializer(OriginLayout.class, new OriginLayout.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(ViewportLayout.class, new ViewportLayout.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(FlowLayout.class, new FlowLayout.XMLDeserializer());
    }



    public Screen loadFAML(String namespace, String uiPosition) {
        if (uiPosition.endsWith(".xml")) {
            Document dom = ResourceManager.instance.getResource(ResourceLocation.uiScreen(namespace, uiPosition)).getAsDom();
            Element faml = (Element) dom.getElementsByTagName("faml").item(0);
            if (!faml.getAttribute("ext").equals("cubecraft_ui")) {
                throw new RuntimeException("invalid ui xml");
            }
            return Registries.FAML_READER.deserialize(faml, Screen.class);

        } else {
            throw new RuntimeException("loaded a none exist file!");
        }
    }



    public void registerComponent(String id, Class<? extends Node> clazz, FAMLDeserializer<?> deserializer) {
        Registries.FAML_READER.registerDeserializer(clazz, deserializer);
        this.componentClassMapping.put(id, clazz);
    }

    public void registerLayout(String id, Class<? extends LayoutManager> clazz, FAMLDeserializer<?> deserializer) {
        Registries.FAML_READER.registerDeserializer(clazz,deserializer);
        this.layoutClassMapping.put(id,clazz);
    }

    public void registerRenderController(Class<? extends Component> clazz, ResourceLocation loc){
        this.renderControllerLocations.put(clazz,loc);
    }

    public void registerRendererPart(String id, Class<? extends ComponentPartRenderer> clazz, JsonDeserializer<?> deserializer){
        this.rendererPartClassMapping.put(id,clazz);
        Registries.GSON_BUILDER.registerTypeAdapter(clazz,deserializer);
    }



    public Set<String> getCompIdList() {
        return this.componentClassMapping.keySet();
    }

    public Class<?> getLayoutClass(String type) {
        return this.layoutClassMapping.get(type);
    }

    public Class<? extends Node> getCompClass(String name) {
        return this.componentClassMapping.get(name);
    }

    public ComponentRenderer getRenderController(Class<? extends Node> clazz) {
        return this.renderers.get(clazz);
    }

    public Class<? extends ComponentPartRenderer> getRendererPartClass(String id) {
        return this.rendererPartClassMapping.get(id);
    }



    public void initializeUIRenderController() {
        Registries.GSON_BUILDER.registerTypeAdapter(ComponentRenderer.class, new ComponentRenderer.JDeserializer());
        Registries.GSON_BUILDER.registerTypeAdapter(ComponentPartRenderer.class, new ComponentPartRenderer.JDeserializer());
        Gson gson = Registries.createJsonReader();
        CollectionUtil.iterateMap(this.renderControllerLocations, (key, item) -> this.renderers.put(key,gson.fromJson(
                ResourceManager.instance.getResource(item).getAsText(), ComponentRenderer.class)
        ));
        List<ResourceLocation> locations = new ArrayList<>();
        CollectionUtil.iterateMap(this.renderers, ((key, item) -> item.initializeModel(locations)));
        for (ResourceLocation loc : locations) {
            Registries.TEXTURE.createTexture2D(ResourceManager.instance.getResource(loc), false, false);
        }
    }

    private boolean legacyFontRequest=false;

    public void requestLegacyFont(){
        this.legacyFontRequest=true;
    }

    public boolean isLegacyFontRequested() {
        return this.legacyFontRequest;
    }
}
