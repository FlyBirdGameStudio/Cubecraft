package io.flybird.cubecraft.client.gui.layout;

import io.flybird.util.file.faml.FAMLDeserializer;
import io.flybird.util.file.faml.XmlReader;
import com.google.gson.Gson;
import org.w3c.dom.Element;

public class OriginLayout extends LayoutManager{
    public enum Origin{
        LEFT_TOP,
        LEFT_MIDDLE,
        LEFT_BOTTOM,
        MIDDLE_MIDDLE,
        MIDDLE_TOP,
        MIDDLE_BOTTOM,
        RIGHT_TOP,
        RIGHT_MIDDLE,
        RIGHT_BOTTOM;

        public static Origin from(String meta) {
            return switch (meta){
                case "left_top"->LEFT_TOP;
                case "left_middle"->LEFT_MIDDLE;
                case "left_bottom"->LEFT_BOTTOM;
                case "middle_top"->MIDDLE_TOP;
                case "middle_middle"->MIDDLE_MIDDLE;
                case "middle_bottom"->MIDDLE_BOTTOM;
                case "right_top"->RIGHT_TOP;
                case "right_middle"->RIGHT_MIDDLE;
                case "right_bottom"->RIGHT_BOTTOM;
                default -> MIDDLE_MIDDLE;
            };
        }
    }

    public OriginLayout(int rx,int ry,int width,int height,Origin origin,int layer){
        this.rx=rx;
        this.ry=ry;
        this.width=width;
        this.height=height;
        this.origin=origin;
        this.layer=layer;
    }

    public Origin origin;
    public int rx,ry;

    @Override
    public void resize(int x,int y,int scrWidth, int scrHeight) {
        int ox=0,oy=0;
        switch (this.origin) {
            case LEFT_TOP -> {
                ox = 0;
                oy = 0;
            }
            case LEFT_MIDDLE -> {
                ox = 0;
                oy = scrHeight / 2 - height / 2;
            }
            case LEFT_BOTTOM -> {
                ox = 0;
                oy = scrHeight - height;
            }

            case MIDDLE_TOP -> {
                ox = scrWidth / 2 - width / 2;
                oy = 0;
            }
            case MIDDLE_MIDDLE -> {
                ox = scrWidth / 2 - width / 2;
                oy = scrHeight / 2 - height / 2;
            }
            case MIDDLE_BOTTOM -> {
                ox = scrWidth / 2 - width / 2;
                oy = scrHeight - height;
            }
            case RIGHT_TOP -> {
                ox = scrWidth - width;
                oy = 0;
            }
            case RIGHT_MIDDLE -> {
                ox = scrWidth - width;
                oy = scrHeight / 2 - height / 2;
            }
            case RIGHT_BOTTOM -> {
                ox = scrWidth - width;
                oy = scrHeight - height;
            }
        }
        this.ax = ox + rx;
        this.ay = oy + ry;
        this.ax+= this.getBorder().left;
        this.ay+= this.getBorder().top;
        this.aWidth=this.width- this.getBorder().right;
        this.aHeight=this.height- this.getBorder().bottom;
    }


    public static class XMLDeserializer implements FAMLDeserializer<OriginLayout> {
        @Override
        public OriginLayout deserialize(Element element, XmlReader famlLoadingContext) {
            Origin side= Origin.from(element.getElementsByTagName("type").item(0).getTextContent());
            int[] l=new Gson().fromJson(element.getElementsByTagName("layout").item(0).getTextContent(),int[].class);
            int[] l2=new Gson().fromJson(element.getElementsByTagName("border").item(0).getTextContent(),int[].class);
            OriginLayout layout=new OriginLayout(l[0],l[1],l[2],l[3],side,0);
            layout.setBorder(new Border(l2[0],l2[1],l2[2],l2[3]));
            return layout;
        }
    }
}
