package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.client.gui.component.*;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.layout.FlowLayout;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.client.gui.layout.OriginLayout;
import io.flybird.cubecraft.client.gui.layout.ViewportLayout;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.util.file.faml.FAMLDeserializer;
import io.flybird.util.file.faml.XmlReader;
import io.flybird.util.file.lang.Language;
import com.google.gson.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class ScreenLoader {

    private static Component getComponent(JsonObject comp) {
        return switch (comp.get("type").getAsString()) {
            case "button" -> new Button(
                    comp.get("text_color").getAsInt(),
                    Language.get(comp.get("text").getAsString())
            );
            case "label" -> new Label(
                    new Text(
                            Language.get(comp.get("text").getAsString()),
                            comp.get("text_color").getAsInt(),
                            FontAlignment.from(comp.get("text_alignment").getAsString())
                    )
            );
            default -> throw new IllegalArgumentException("unknown component id:" + comp.get("type"));
        };
    }

    private static LayoutManager getLayout(JsonObject layout) {
        JsonArray arr = layout.get("layout").getAsJsonArray();
        return switch (layout.get("type").getAsString()) {
            case "origin" -> new OriginLayout(
                    arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt(), arr.get(3).getAsInt(),
                    OriginLayout.Origin.from(layout.get("meta").getAsString()),
                    layout.get("layer").getAsInt()
            );
            case "viewport" -> new ViewportLayout(
                    arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt(), arr.get(3).getAsInt(),
                    layout.get("layer").getAsInt()
            );
            default -> null;
        };
    }

    private static XmlReader famlLoader = new XmlReader();
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Gson jsonLoader;

    public static void registerObjectDeserializer(Type t, FAMLDeserializer faml, JsonDeserializer json) {
        gsonBuilder.registerTypeAdapter(t, json);
        famlLoader.registerDeserializer(t, faml);
        jsonLoader = gsonBuilder.create();
    }

    public static void initialize() {
        //basic
        registerObjectDeserializer(Text.class, new Text.XMLDeserializer(), new Text.JDeserializer());
        registerObjectDeserializer(LayoutManager.class, new LayoutManager.XMLDeserializer(), new LayoutManager.JDeserializer());
        registerObjectDeserializer(Screen.class, new Screen.XMLDeserializer(), new Screen.JDeserializer());

        //layout
        famlLoader.registerDeserializer(OriginLayout.class, new OriginLayout.XMLDeserializer());
        famlLoader.registerDeserializer(ViewportLayout.class, new ViewportLayout.XMLDeserializer());
        famlLoader.registerDeserializer(FlowLayout.class, new FlowLayout.XMLDeserializer());

        famlLoader.registerDeserializer(TopBar.class, new TopBar.XMLDeserializer());

        //controls
        registerObjectDeserializer(Button.class, new Button.XMLDeserializer(), new Button.JDeserializer());
        registerObjectDeserializer(Label.class, new Label.XMLDeserializer(), new Label.JDeserializer());
        registerObjectDeserializer(ImageRenderer.class, new ImageRenderer.XMLDeserializer(), new ImageRenderer.JDeserializer());
        registerObjectDeserializer(SplashText.class, new SplashText.XMLDeserializer(), new SplashText.JDeserializer());
    }

    static {
        initialize();
    }


    public static Gson getSharedJsonLoader() {
        return jsonLoader;
    }

    public static XmlReader getSharedXMLReader() {
        return famlLoader;
    }


    /**
     * load a screen from xml file
     *
     * @param path resource location
     * @return loaded screen object
     */
    private static Screen loadFromXML(String namespace,String path) {
        Document dom = ResourceManager.instance.getResource(ResourceLocation.uiScreen(namespace, path)).getAsDom();
        Element faml = (Element) dom.getElementsByTagName("faml").item(0);
        if (!faml.getAttribute("ext").equals("cubecraft_ui")) {
            throw new RuntimeException("invalid ui xml");
        }
        return famlLoader.deserialize(faml, Screen.class);

    }

    /**
     * load an ui file,using ext name to manifest load type(xml/json)
     *
     * @param uiPosition resource location
     * @return loaded screen object
     */
    public static Screen loadByExtName(String namespace,String uiPosition) {
        if (uiPosition.endsWith(".xml")) {
            return loadFromXML(namespace,uiPosition);
        } else {
            return null;
            //return loadFromJson(uiPosition);
        }
    }
}
