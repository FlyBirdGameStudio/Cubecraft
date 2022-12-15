package io.flybird.cubecraft.internal.ui.component;



import io.flybird.cubecraft.client.gui.base.SmoothedFontRenderer;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public class Label extends Component {
    public Text text;
    public int color;
    public Label(Text text) {
        this.text=text;
    }



    @Override
    public void render() {
        SmoothedFontRenderer.render(text.getText(),layoutManager.ax,layoutManager.ay, text.getColor(), layoutManager.aHeight,text.getAlignment());
    }


    public static class XMLDeserializer implements FAMLDeserializer<Label> {
        @Override
        public Label deserialize(Element element, XmlReader famlLoadingContext) {
            Label label=new Label(famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0),Text.class));
            label.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return label;
        }
    }


    public void setText(Text text) {
        this.text = text;
    }
}
