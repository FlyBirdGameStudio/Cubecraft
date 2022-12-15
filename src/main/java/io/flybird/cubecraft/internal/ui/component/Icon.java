package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.cubecraft.client.gui.font.FontAlignment;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import io.flybird.util.math.MathHelper;
import org.w3c.dom.Element;

public class Icon extends Component {
    final char icon;
    final int color;
    final int size;
    final FontAlignment alignment;

    public Icon(char icon, int color, int size, FontAlignment alignment) {
        this.icon = icon;
        this.color = color;
        this.size = size;
        this.alignment = alignment;
    }

    @Override
    public void render() {
        ClientRegistries.ICON_FONT_RENDERER.render(String.valueOf(icon), layoutManager.ax, layoutManager.ay, color, size, alignment);
    }

    public static class XMLDeserializer implements FAMLDeserializer<Icon> {
        @Override
        public Icon deserialize(Element element, XmlReader famlLoadingContext) {
            Icon icon= new Icon(
                    (char) MathHelper.hex2Int(element.getAttribute("icon")),
                    MathHelper.hex2Int(element.getAttribute("color")),
                    Integer.parseInt(element.getAttribute("size")),
                    FontAlignment.from(element.getAttribute("align"))
            );
            icon.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return icon;
        }
    }
}
