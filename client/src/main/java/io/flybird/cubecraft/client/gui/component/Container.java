package io.flybird.cubecraft.client.gui.component;

import io.flybird.util.container.CollectionUtil;
import io.flybird.util.container.MultiMap;

public abstract class Container extends Node{
    private final MultiMap<String, Node> nodes=new MultiMap<>();

    @Override
    public void onResize(int x, int y, int w, int h) {
        super.onResize(x, y, w, h);
        CollectionUtil.iterateMap(this.nodes, (key, item) -> item.onResize(this.layoutManager.ax,this.layoutManager.ay,this.layoutManager.aWidth,this.layoutManager.aHeight));
    }

    @Override
    public void render() {
        CollectionUtil.iterateMap(this.nodes, (key, item) -> item.render());
    }

    @Override
    public void tick(){
        CollectionUtil.iterateMap(this.nodes,(key, item) -> item.tick());
    }

    public void addChild(String name,Node node){
        node.setParent(this);
        this.nodes.put(name,node);
    }

    public void removeChild(String id){
        this.nodes.get(id).setParent(null);
        this.nodes.remove(id);
    }
}
