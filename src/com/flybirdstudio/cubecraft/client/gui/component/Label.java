package com.flybirdstudio.cubecraft.client.gui.component;


import com.flybirdstudio.cubecraft.client.gui.FontAlignment;
import com.flybirdstudio.cubecraft.client.gui.FontRenderer;

public class Label extends Component {
    public String text;
    public int size=16;
    public int color;

    private FontAlignment alignment;
    public Label(String text, int color, FontAlignment alignment) {
        this.text=text;
        this.color=color;
        this.alignment=alignment;
    }


    @Override
    public void render() {
        FontRenderer.renderShadow(text,layoutManager.ax,layoutManager.ay,color,layoutManager.aHeight, this.alignment);
    }

}
