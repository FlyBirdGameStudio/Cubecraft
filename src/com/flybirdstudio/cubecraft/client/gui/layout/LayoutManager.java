package com.flybirdstudio.cubecraft.client.gui.layout;

import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public abstract class LayoutManager {
    public int ax,ay;
    public int width,height;
    public int layer;
    public int aWidth;
    public int aHeight;

    public abstract void resize(int scrWidth,int scrHeight);
    public Border border=new Border(0,0,0,0);

    public static class XMLDeserializer implements FAMLDeserializer<LayoutManager> {
        @Override
        public LayoutManager deserialize(Element element, XmlReader famlLoadingContext) {
            return ScreenLoader.getSharedJsonLoader().fromJson(element.getTextContent(),LayoutManager.class);
        }
    }

    public static class JDeserializer implements JsonDeserializer<LayoutManager>{
        @Override
        public LayoutManager deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject layout=jsonElement.getAsJsonObject();
            JsonArray arr = layout.get("layout").getAsJsonArray();

            LayoutManager layoutManager= switch (layout.get("type").getAsString()) {
                case "origin" -> new OriginLayout(
                        arr.get(0).getAsInt(),
                        arr.get(1).getAsInt(),
                        arr.get(2).getAsInt(),
                        arr.get(3).getAsInt(),
                        OriginLayout.Origin.from(layout.get("meta").getAsString()),
                        layout.get("layer").getAsInt()
                );
                case "viewport" -> new ViewportLayout(
                        arr.get(0).getAsInt(),
                        arr.get(1).getAsInt(),
                        arr.get(2).getAsInt(),
                        arr.get(3).getAsInt(),
                        layout.get("layer").getAsInt()
                );

                default -> null;
            };
            if(layout.has("border")) {
                JsonArray border = layout.get("border").getAsJsonArray();
                layoutManager.border = new Border(
                        border.get(0).getAsInt(),
                        border.get(1).getAsInt(),
                        border.get(2).getAsInt(),
                        border.get(3).getAsInt()
                );
            }
            return layoutManager;
        }
    }
}
