package com.flybirdstudio.cubecraft.client.gui.component;


import com.flybirdstudio.cubecraft.client.gui.layout.LayoutManager;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.google.gson.*;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class ImageRenderer extends Component {
    private final Texture2D texture=new Texture2D(false,false);
    private String file;
    public HorizontalClipping hClip;
    public VerticalClipping vClip;
    public ImageRenderer(String file,HorizontalClipping hClip,VerticalClipping vClip) {
        this.hClip=hClip;
        this.vClip=vClip;
        this.file=file;
        this.texture.generateTexture();
        this.texture.load(file);
        this.texture.bind();
    }

    @Override
    public void render() {
        this.texture.bind();
        float u0=0,u1=0,v0=0,v1=0;
        switch (this.vClip){
            case UP -> {
                v0=0;
                v1=this.layoutManager.height*1.0f/this.texture.getHeight();
            }
            case MIDDLE -> {
                v0=(this.texture.getHeight()/2.0f-this.layoutManager.height/2.0f)/this.texture.getHeight();
                v1=(this.texture.getHeight()/2.0f+this.layoutManager.height/2.0f)/this.texture.getHeight();
            }
            case DOWN -> {
                v0=(this.texture.getHeight()/2.0f-this.layoutManager.height/2.0f)/this.texture.getHeight();
                v1= 1;
            }
        }
        switch (this.hClip){
            case LEFT -> {
                u0=0;
                u1=(this.layoutManager.width*1.0f/this.texture.getWidth())/this.texture.getWidth();
            }
            case MIDDLE -> {
                u0=(this.texture.getWidth()/2.0f-this.layoutManager.width/2.0f)/this.texture.getWidth();
                u1=(this.texture.getWidth()/2.0f+this.layoutManager.width/2.0f)/this.texture.getWidth();
            }
            case RIGHT -> {
                u0=(this.texture.getWidth()/2.0f-this.layoutManager.width/2.0f)/this.texture.getWidth();
                u1=1;
            }
        }
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();

        ShapeRenderer.drawRectUV(layoutManager.ax,
                layoutManager.ax+layoutManager.width,
                layoutManager.ay,
                layoutManager.ay+layoutManager.height,
                layer,layer,u0,u1,v0,v1);
        ShapeRenderer.end();
    }

    public static class XMLDeserializer implements FAMLDeserializer<ImageRenderer> {
        @Override
        public ImageRenderer deserialize(Element element, XmlReader famlLoadingContext) {
            ImageRenderer imageRenderer=new ImageRenderer(
                    element.getAttribute("img"),
                    HorizontalClipping.from(element.getAttribute("h-clip")),
                    VerticalClipping.from(element.getAttribute("v-clip"))
            );
            imageRenderer.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return imageRenderer;
        }
    }

    public static class JDeserializer implements JsonDeserializer<ImageRenderer>{
        @Override
        public ImageRenderer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject node=jsonElement.getAsJsonObject();
            ImageRenderer imageRenderer=new ImageRenderer(
                    node.get("image").getAsString(),
                    HorizontalClipping.from(node.get("h-clip").getAsString()),
                    VerticalClipping.from(node.get("v-clip").getAsString())
            );
            imageRenderer.setLayout(jsonDeserializationContext.deserialize(node.get("layout"),LayoutManager.class));
            return imageRenderer;
        }
    }

    public enum VerticalClipping{
        UP,
        MIDDLE,
        DOWN;

        public static VerticalClipping from(String attribute) {
            return switch (attribute){
                case "up"->UP;
                case "middle"->MIDDLE;
                case  "down"->DOWN;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(attribute));
            };
        }
    }

    public enum HorizontalClipping {
        LEFT,
        MIDDLE,
        RIGHT;

        public static HorizontalClipping from(String attribute) {
            return switch (attribute){
                case "left"->LEFT;
                case "middle"->MIDDLE;
                case  "right"->RIGHT;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(attribute));
            };
        }

    }
}
