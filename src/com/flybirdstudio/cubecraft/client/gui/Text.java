package com.flybirdstudio.cubecraft.client.gui;

import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.flybirdstudio.util.file.lang.Language;
import com.flybirdstudio.util.math.MathHelper;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Text {
    private String text;
    private int color;
    private FontAlignment alignment;

    public Text(String text, int color, FontAlignment alignment) {
        this.text = text;
        this.color = color;
        this.alignment = alignment;
    }

    public FontAlignment getAlignment() {
        return alignment;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAlignment(FontAlignment alignment) {
        this.alignment = alignment;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static class XMLDeserializer implements FAMLDeserializer<Text> {
        @Override
        public Text deserialize(Element element, XmlReader famlLoadingContext) {
            return new Text(
                    getText(element.getAttribute("type"), element.getTextContent()),
                    MathHelper.hex2Int(element.getAttribute("color")),
                    FontAlignment.from(element.getAttribute("align"))
            );
        }
    }

    public static class JDeserializer implements JsonDeserializer<Text> {
        @Override
        public Text deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject obj = jsonElement.getAsJsonObject();
            return new Text(
                    getText(obj.get("type").getAsString(), obj.get("src").getAsString()),
                    Integer.parseInt(obj.get("color").getAsString()),
                    FontAlignment.from(obj.get("align").getAsString())
            );
        }
    }

    private static String getText(String type, String src) {
        return switch (type) {
            case "raw" -> src;
            case "lang" -> Language.get(src);
            case "random" -> {
                String[] splash;
                try {
                    splash = new Gson().fromJson(new String(ResourceManager.instance.getResource(src, src).readAllBytes(), StandardCharsets.UTF_8), String[].class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                yield splash[new Random().nextInt(splash.length)];
            }
            default -> throw new IllegalArgumentException("no matched constant named %s".formatted());
        };
    }

}
