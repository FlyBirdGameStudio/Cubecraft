package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.internal.ui.layout.FlowLayout;
import io.flybird.cubecraft.internal.ui.layout.OriginLayout;
import io.flybird.cubecraft.internal.ui.layout.ViewportLayout;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import com.google.gson.*;
import org.w3c.dom.Element;

import java.lang.reflect.Type;

public abstract class LayoutManager {
    public int ax,ay;
    public int width,height;
    public int layer;
    public int aWidth;
    public int aHeight;

    public abstract void resize(int x,int y,int scrWidth,int scrHeight);
    protected Border border=new Border(0,0,0,0);

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public static class XMLDeserializer implements FAMLDeserializer<LayoutManager> {
        @Override
        public LayoutManager deserialize(Element element, XmlReader famlLoadingContext) {
            return Registries.FAML_READER.deserialize(element,Registries.GUI_MANAGER.getLayoutClass(element.getAttribute("type")));
        }
    }
}
