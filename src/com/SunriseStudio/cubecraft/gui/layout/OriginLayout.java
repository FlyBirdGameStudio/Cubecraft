package com.SunriseStudio.cubecraft.gui.layout;

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
        RIGHT_BOTTOM
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
    public void resize(int scrWidth, int scrHeight) {
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
        this.ax+=this.border.left;
        this.ay+=this.border.top;
        this.aWidth=this.width-this.border.right;
        this.aHeight=this.height-this.border.bottom;
    }
}
