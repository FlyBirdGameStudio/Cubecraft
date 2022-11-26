package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.client.gui.Node;
import io.flybird.cubecraft.client.gui.layout.Border;
import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.cubecraft.client.gui.screen.Screen;


public abstract class Component extends Node {
    protected Screen parent;
    public boolean[] scaleEnabled={false,false,false,false};
    protected int layer;
    //layout

    public void resize(int width,int height){

        this.layoutManager.resize(0,0,width,height);
        this.layer=layoutManager.layer;

    }

    public abstract void render();

    public void setLayout(LayoutManager layoutManager){
        this.layoutManager=layoutManager;
    }

    @Deprecated()//move to layout manager
    public void setScaleEnabled(boolean l,boolean r,boolean t,boolean b){
        this.scaleEnabled[0]=l;
        this.scaleEnabled[1]=r;
        this.scaleEnabled[2]=b;
        this.scaleEnabled[3]=t;
    }

    @Deprecated
    public void setBorder(Border border) {
        this.layoutManager.setBorder(border);
    }

    public Screen getParent() {
        return parent;
    }

    public void setParent(Screen parent) {
        this.parent = parent;
    }

    public String getID() {
        return this.getParent().getComponents().of(this);
    }
}
