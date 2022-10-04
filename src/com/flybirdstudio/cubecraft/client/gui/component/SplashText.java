package com.flybirdstudio.cubecraft.client.gui.component;

import com.flybirdstudio.cubecraft.client.gui.FontRenderer;
import com.flybirdstudio.cubecraft.client.gui.Text;
import com.flybirdstudio.cubecraft.client.gui.layout.LayoutManager;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.google.gson.*;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class SplashText extends Component{
    final Text splashText;
    final int rotation;
    final boolean bobbing;

    public SplashText(Text splashText, int rotation, boolean bobbing){
        this.splashText=splashText;
        this.rotation=rotation;
        this.bobbing=bobbing;
    }

    @Override
    public void render() {
        GL11.glTranslatef(this.layoutManager.ax,this.layoutManager.ay,0);
        double sin=Math.sin(System.currentTimeMillis()/300d)*0.1+1.1;
        if(this.bobbing) {
            GL11.glScaled(sin, sin, sin);
        }
        GL11.glRotatef(rotation,0,0,1);
        FontRenderer.renderShadow(splashText.getText(),0,0,this.splashText.getColor(),12, this.splashText.getAlignment());
    }

    public static class XMLDeserializer implements FAMLDeserializer<SplashText>{
        @Override
        public SplashText deserialize(Element element, XmlReader famlLoadingContext) {
            SplashText splashText= new SplashText(
                    famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0),Text.class),
                    Integer.parseInt(element.getAttribute("rotation")),
                    Boolean.parseBoolean(element.getAttribute("bobbing"))
            );
            splashText.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return splashText;
        }
    }

    public static class JDeserializer implements JsonDeserializer<SplashText>{
        @Override
        public SplashText deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject obj=jsonElement.getAsJsonObject();
            SplashText splashText= new SplashText(
                    jsonDeserializationContext.deserialize(obj.get("text"),Text.class),
                    obj.get("rotation").getAsInt(),
                    obj.get("bobbing").getAsBoolean()
            );
            splashText.setLayout(jsonDeserializationContext.deserialize(obj.get("layout"), LayoutManager.class));
            return splashText;
        }
    }
}
