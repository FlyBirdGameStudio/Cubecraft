package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.base.SmoothedFontRenderer;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.*;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class SplashText extends Component {
    final Text splashText;
    final int rotation;
    final boolean bobbing;

    public SplashText(Text splashText, int rotation, boolean bobbing){
        this.splashText=splashText;
        this.rotation=rotation;
        this.bobbing=bobbing;
    }

    public Text getSplashText() {
        return splashText;
    }

    @Override
    public void render() {
        GL11.glTranslatef(this.layoutManager.ax,this.layoutManager.ay,0);
        double sin=Math.sin(System.currentTimeMillis()/300d)*0.1+1.1;
        if(this.bobbing) {
            GL11.glScaled(sin, sin, sin);
        }
        GL11.glRotatef(rotation,0,0,1);
        SmoothedFontRenderer.renderShadow(splashText.getText(),0,0,this.splashText.getColor(),12, this.splashText.getAlignment());
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
}
