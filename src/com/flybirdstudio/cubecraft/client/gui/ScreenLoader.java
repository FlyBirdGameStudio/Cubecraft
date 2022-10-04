package com.flybirdstudio.cubecraft.client.gui;

import com.flybirdstudio.cubecraft.client.gui.component.*;
import com.flybirdstudio.cubecraft.client.gui.layout.*;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.flybirdstudio.util.file.lang.Language;
import com.google.gson.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ScreenLoader {

    private static final HashMap<String,Screen>loadedScreen=new HashMap<>();
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(HashMap.class, (JsonDeserializer<HashMap<String, Component>>) (jsonElement, type, jsonDeserializationContext) -> {
        HashMap<String, Component> list = new HashMap<>();
        for (JsonElement e : jsonElement.getAsJsonArray()) {
            JsonObject comp = e.getAsJsonObject();
            Component p = getComponent(comp);
            p.setLayout(getLayout(comp.get("layout").getAsJsonObject()));
            JsonArray arr = comp.get("border").getAsJsonArray();
            p.setBorder(new Border(arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt(), arr.get(3).getAsInt()));
            list.put(comp.get("id").getAsString(), p);
        }
        return list;
    }).create();

    public static HashMap<String, Component> load(String file) {
        String json;
        try {
            json = new String(ResourceManager.instance.getResource(file, "/resource/fallback/ui.json").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gson.fromJson(json, HashMap.class);
    }

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
     * @param path resource location
     * @return loaded screen object
     */
    private static Screen loadFromXML(String path) {
        try {
            Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ResourceManager.instance.getResource(path, "/resource/ui/fallback.xml"));

            Element faml = (Element) dom.getElementsByTagName("faml").item(0);
            if (!faml.getAttribute("ext").equals("cubecraft_ui")) {
                throw new RuntimeException("invalid ui xml");
            }
            return famlLoader.deserialize(faml, Screen.class);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * load a screen from json file
     * @param path resource location
     * @return loaded screen object
     */
    private static Screen loadFromJson(String path){
        try {
            return jsonLoader.fromJson(new String(ResourceManager.instance.getResource(path,"").readAllBytes()),Screen.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * load an ui file,using ext name to manifest load type(xml/json)
     * @param uiPosition resource location
     * @return loaded screen object
     */
    public static Screen loadByExtName(String uiPosition) {
        if(uiPosition.endsWith(".xml")){
            return loadFromXML(uiPosition);
        }else {
            return loadFromJson(uiPosition);
        }
    }
}
