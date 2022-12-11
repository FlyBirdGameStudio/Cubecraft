package io.flybird.cubecraft.internal.ui.layout;

import io.flybird.cubecraft.client.gui.layout.Border;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.Gson;
import org.w3c.dom.Element;

public class ViewportLayout extends LayoutManager {
    public float left, right, bottom, top;

    public ViewportLayout(int left, int right, int bottom, int top, int layer) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        this.layer = layer;
    }

    @Override
    public void resize(int x, int y, int scrWidth, int scrHeight) {
        this.ax = (int) (left / 100 * scrWidth);
        this.ay = (int) (top / 100 * scrHeight);
        this.width = (int) (right / 100 * scrWidth - ax);
        this.height = (int) (bottom / 100 * scrHeight - ay);
        this.ax += this.getBorder().left;
        this.ay += this.getBorder().top;
        this.width -= this.getBorder().right * 2;
        this.height -= this.getBorder().bottom * 2;
    }

    public static class XMLDeserializer implements FAMLDeserializer<ViewportLayout> {
        @Override
        public ViewportLayout deserialize(Element element, XmlReader famlLoadingContext) {
            int[] l = new Gson().fromJson(element.getElementsByTagName("layout").item(0).getTextContent(), int[].class);
            int[] l2 = new Gson().fromJson(element.getElementsByTagName("border").item(0).getTextContent(), int[].class);
            ViewportLayout layout = new ViewportLayout(l[0], l[1], l[2], l[3], 0);
            layout.setBorder(new Border(l2[0], l2[1], l2[2], l2[3]));
            return layout;
        }
    }
}
