package io.flybird.cubecraft.internal.ui.layout;

import io.flybird.cubecraft.client.gui.layout.Border;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.Gson;
import org.w3c.dom.Element;

public class FlowLayout extends LayoutManager {
    private final FlowSide side;
    private final int pos,length;

    public FlowLayout(FlowSide side, int pos, int length) {
        this.side = side;
        this.pos = pos;
        this.length = length;
    }

    @Override
    public void resize(int x, int y, int scrWidth, int scrHeight) {
        switch (this.side){
            case TOP -> {
                this.ay=y+pos;
                this.aWidth=scrWidth;
                this.ax=x;
                this.aHeight=length;
            }
            case BOTTOM -> {
                this.ay=x+scrHeight-pos;
                this.aWidth=scrWidth;
                this.ax=x;
                this.aHeight=length;
            }
            case LEFT -> {
                this.ax=x+pos;
                this.aHeight=scrHeight;
                this.ay=y;
                this.aWidth=length;
            }
            case RIGHT-> {
                this.ax=x+scrWidth-pos;
                this.aHeight=scrHeight;
                this.ay=y;
                this.aWidth=length;
            }
        }
    }

    public static class XMLDeserializer implements FAMLDeserializer<FlowLayout> {
        @Override
        public FlowLayout deserialize(Element element, XmlReader famlLoadingContext) {
            FlowSide side=FlowSide.fromID(element.getElementsByTagName("type").item(0).getTextContent());
            int[] l=new Gson().fromJson(element.getElementsByTagName("layout").item(0).getTextContent(),int[].class);
            int[] l2=new Gson().fromJson(element.getElementsByTagName("border").item(0).getTextContent(),int[].class);
            FlowLayout layout=new FlowLayout(side,l[1],l[0]);
            layout.setBorder(new Border(l2[0],l2[1],l2[2],l2[3]));
            return layout;
        }
    }

    public enum FlowSide{
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

        public static FlowSide fromID(String id){
            return switch (id){
                case "left"->LEFT;
                case "right"->RIGHT;
                case "bottom"->BOTTOM;
                case "top"->TOP;
                default ->throw new IllegalArgumentException("no matched constant named "+id);
            };
        }
    }
}