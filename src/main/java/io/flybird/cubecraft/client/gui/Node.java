package io.flybird.cubecraft.client.gui;

import io.flybird.cubecraft.client.gui.layout.LayoutManager;
import io.flybird.util.event.EventListener;

public abstract class Node implements EventListener {
    protected LayoutManager layoutManager;
    protected Container parent;

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public Container getParent() {
        return parent;
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void onResize(int x, int y, int w, int h){
        this.layoutManager.resize(x,y,w,h);
    }

    public abstract void render();

    public void tick(){}

    public String getStatement() {
        return "default";
    }

    public Text queryText(String query) {
        return new Text("",0,FontAlignment.MIDDLE);
    }
}
