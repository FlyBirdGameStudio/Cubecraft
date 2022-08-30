package com.flybirdstudio.cubecraft.client.render.model;

public enum RenderType {
    ALPHA,
    TRANSPARENT;

    public static RenderType from(String id){
        return switch (id){
            case "alpha"->ALPHA;
            case "blend"->TRANSPARENT;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }
}
