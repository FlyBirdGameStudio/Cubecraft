package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Set;

public class ScreenLoader {
    private final HashMap<String, Class<? extends Node>> componentClassMapping = new HashMap<>();
    private final HashMap<String, Class<? extends LayoutManager>> layoutClassMapping = new HashMap<>();

    public ScreenLoader(){
        //basic
        Registries.FAML_READER.registerDeserializer(Text.class, new Text.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(LayoutManager.class, new LayoutManager.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(Screen.class, new Screen.XMLDeserializer());

        //layout
        Registries.FAML_READER.registerDeserializer(OriginLayout.class, new OriginLayout.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(ViewportLayout.class, new ViewportLayout.XMLDeserializer());
        Registries.FAML_READER.registerDeserializer(FlowLayout.class, new FlowLayout.XMLDeserializer());
    }

    public XmlReader getSharedXMLReader() {
        return Registries.FAML_READER;
    }

    public Screen loadByExtName(String namespace, String uiPosition) {
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

    public Class<? extends Node> getCompClass(String name) {
        return this.componentClassMapping.get(name);
    }

    public void registerComponent(String id, Class<? extends Node> clazz, FAMLDeserializer<?> deserializer) {
        Registries.FAML_READER.registerDeserializer(clazz, deserializer);
        this.componentClassMapping.put(id, clazz);
    }

    public Set<String> getCompIdList() {
        return this.componentClassMapping.keySet();
    }

    public void registerLayout(String id, Class<? extends LayoutManager> clazz, FAMLDeserializer<?> deserializer) {
        Registries.FAML_READER.registerDeserializer(clazz,deserializer);
        this.layoutClassMapping.put(id,clazz);
    }

    public Class<?> getLayoutClass(String type) {
        return this.layoutClassMapping.get(type);
    }
}
