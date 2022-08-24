package com.sunrisestudio.cubecraft.client.gui;

import com.google.gson.*;
import com.sunrisestudio.cubecraft.client.gui.component.Button;
import com.sunrisestudio.cubecraft.client.gui.component.Component;
import com.sunrisestudio.cubecraft.client.gui.component.Label;
import com.sunrisestudio.cubecraft.client.gui.layout.Border;
import com.sunrisestudio.cubecraft.client.gui.layout.LayoutManager;
import com.sunrisestudio.cubecraft.client.gui.layout.OriginLayout;
import com.sunrisestudio.cubecraft.client.gui.layout.ViewportLayout;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.util.file.lang.Language;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ScreenLoader {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(HashMap.class, (JsonDeserializer<HashMap<String, Component>>) (jsonElement, type, jsonDeserializationContext) -> {
        HashMap<String, Component> list = new HashMap<>();
        for (JsonElement e : jsonElement.getAsJsonArray()) {
            JsonObject comp = e.getAsJsonObject();
            Component p=getComponent(comp);
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

    private static Component getComponent(JsonObject comp){
        return switch (comp.get("type").getAsString()) {
            case "button"-> new Button(
                    0,
                    comp.get("text_color").getAsInt(),
                    Language.get(comp.get("text").getAsString())
            );
            case "label"-> new Label(
                        Language.get(comp.get("text").getAsString()),
                        comp.get("text_color").getAsInt(),
                        FontAlignment.from(comp.get("text_alignment").getAsString())
            );
            default->throw new IllegalArgumentException("unknown component id:"+comp.get("type"));
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
}
