package com.flybirdstudio.cubecraft.client.gui.layout;

public abstract class LayoutManager {
    public int ax,ay;
    public int width,height;
    public int layer;
    public int aWidth;
    public int aHeight;

    public abstract void resize(int scrWidth,int scrHeight);
    public Border border=new Border(0,0,0,0);
}
