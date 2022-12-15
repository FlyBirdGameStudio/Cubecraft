package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

public class SplashText extends Component {
    final Text text;
    final int rotation;
    final boolean bobbing;

    public SplashText(Text splashText, int rotation, boolean bobbing){
        this.text =splashText;
        this.rotation=rotation;
        this.bobbing=bobbing;
    }

    public Text getText() {
        return text;
    }

    @Override
    public void render() {
        GL11.glTranslatef(this.layoutManager.ax,this.layoutManager.ay,0);
        double sin=Math.sin(System.currentTimeMillis()/300d)*0.1+1.1;
        if(this.bobbing) {
            GL11.glScaled(sin, sin, sin);
        }
        GL11.glRotatef(rotation,0,0,1);
        if(text.isIcon()){
            ClientRegistries.ICON_FONT_RENDERER.render(text.getText(),layoutManager.ax,layoutManager.ay, text.getColor(), layoutManager.aHeight,text.getAlignment());
        }else{
            ClientRegistries.SMOOTH_FONT_RENDERER.render(text.getText(),layoutManager.ax,layoutManager.ay, text.getColor(), layoutManager.aHeight,text.getAlignment());
        }
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
