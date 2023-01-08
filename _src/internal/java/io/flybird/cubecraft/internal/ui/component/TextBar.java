package io.flybird.cubecraft.internal.ui.component;

import io.flybird.cubecraft.client.gui.font.FontAlignment;
import io.flybird.cubecraft.client.gui.base.Text;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.LayoutManager;
import io.flybird.cubecraft.register.Registries;
import io.flybird.quantum3d.event.CharEvent;
import io.flybird.quantum3d.event.MouseClickEvent;
import io.flybird.quantum3d.event.KeyPressEvent;
import io.flybird.quantum3d.event.KeyHoldEvent;
import io.flybird.quantum3d.platform.Keyboard;
import io.flybird.util.event.Event;
import io.flybird.util.event.EventHandler;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.w3c.dom.Element;

import java.util.Objects;

public class TextBar extends Component {
    final StringBuilder text = new StringBuilder();
    private boolean focus;
    private int cursorPos;
    private final int limit;
    private final Text hint;

    public TextBar(int limit, Text hint) {
        this.limit = limit;
        this.hint = hint;
        String s=" "+this.hint.getText();
        this.hint.setText(s);
    }
    
    @EventHandler
    public void onChar(CharEvent e) {
        if (focus&&text.length()<limit) {
            text.insert(cursorPos, e.c());
            cursorPos++;
        }
    }

    private void processPress(int k){
        if (focus) {
            if (k == Keyboard.KEY_BACK && text.length() > 0 && cursorPos > 0) {
                text.deleteCharAt(cursorPos - 1);
                cursorPos--;
            }
            if (k == Keyboard.KEY_RETURN) {
                this.parent.getPlatform().getClientEventBus().callEvent(new SubmitEvent(this.text.toString()));
            }
            if (k == Keyboard.KEY_RIGHT && cursorPos < text.length()) {
                cursorPos++;
            }
            if (k == Keyboard.KEY_LEFT && cursorPos > 0) {
                cursorPos--;
            }
            if (k == Keyboard.KEY_DELETE && cursorPos >=0&&cursorPos<text.length()) {
                text.deleteCharAt(cursorPos);
            }
        }
    }
        
    
    @EventHandler
    public void onKey(KeyHoldEvent e) {
        this.processPress(e.key());
    }

    @EventHandler
    public void onKey(KeyPressEvent e) {
        this.processPress(e.key());
    }

    @EventHandler
    public void onClick(MouseClickEvent e){
        int scale = Registries.CLIENT.getGameSetting().getValueAsInt("client.render.gui.scale", 2);
        int xm = e.x() / scale;
        int ym = e.fixedY() / scale;
        int x0 = this.layoutManager.ax;
        int x1 = x0 + this.layoutManager.aWidth;
        int y0 = this.layoutManager.ay;
        int y1 = y0 + this.layoutManager.aHeight;
        this.focus = xm > x0 && xm < x1 && ym > y0 && ym < y1;
    }

    @Override
    public Text queryText(String query) {
        String cursor = (System.currentTimeMillis() % 1000 < 500) ? "▎" : "";
        if (Objects.equals(query, "cursor")&&focus) {
            return new Text(" " + text.substring(0, cursorPos) + cursor, 0xFFFFFF, FontAlignment.LEFT);
        }
        if(text.length()>0){
            return new Text(" " + text, 0xFFFFFF, FontAlignment.LEFT);
        }
        if(!focus) {
            return this.hint;
        }
        return new Text("",0x000000,FontAlignment.LEFT);
    }

    public static class XMLDeserializer implements FAMLDeserializer<TextBar> {
        @Override
        public TextBar deserialize(Element element, XmlReader famlLoadingContext) {
            TextBar topBar = new TextBar(
                    Integer.parseInt(element.getAttribute("limit")),
                    famlLoadingContext.deserialize((Element) element.getElementsByTagName("text").item(0), Text.class)
            );
            topBar.setLayout(famlLoadingContext.deserialize((Element) element.getElementsByTagName("layout").item(0), LayoutManager.class));
            return topBar;
        }
    }

    record SubmitEvent(String s) implements Event {
    }
}
