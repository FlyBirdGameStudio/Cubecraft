package io.flybird.cubecraft.client.gui.renderer;

import io.flybird.cubecraft.client.gui.Node;

import java.util.HashMap;

public abstract class ComponentRenderer<T extends Node> {
    private final HashMap<String,ComponentPartRenderer[]> renderers=new HashMap<>();

    public void render(T node){
        for (ComponentPartRenderer componentPartRenderer:this.renderers.get(node.getStatement())){

        }
    }
}
