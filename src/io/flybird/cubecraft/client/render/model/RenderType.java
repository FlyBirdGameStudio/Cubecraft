package io.flybird.cubecraft.client.render.model;

import io.flybird.cubecraft.client.render.IRenderType;

public enum RenderType implements IRenderType {
    ALPHA,
    TRANSPARENT;

    public static RenderType from(String id){
        return switch (id){
            case "alpha"->ALPHA;
            case "transparent"->TRANSPARENT;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }
}
