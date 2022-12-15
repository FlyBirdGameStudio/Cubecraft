package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.register.Registries;


public abstract class Component extends Node {
    protected Screen parent;
    protected int layer;
    //layout

    public void resize(int width,int height){

        this.layoutManager.resize(0,0,width,height);
        this.layer=layoutManager.layer;

    }

    public void render(){
        Registries.GUI_MANAGER.getRenderController(this.getClass()).render(this);
    }

    public void setLayout(LayoutManager layoutManager){
        this.layoutManager=layoutManager;
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
