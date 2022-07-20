package com.sunrisestudio.cubecraft.gui.component;


import com.sunrisestudio.cubecraft.gui.FontAlignment;
import com.sunrisestudio.cubecraft.gui.FontRenderer;

public class Label extends Component {
    public String text;
    public int size;
    public int color;

    private FontAlignment alignment;
    public Label(String text, int size, int color, int layer, FontAlignment alignment) {
        this.text=text;
        this.color=color;
        this.size=size;
        this.alignment=alignment;
    }


    @Override
    public void render() {
        FontRenderer.render(text,layoutManager.ax,layoutManager.ay,color,size, this.alignment);
    }

}
