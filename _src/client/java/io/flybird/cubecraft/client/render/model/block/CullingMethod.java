package io.flybird.cubecraft.client.render.model.block;

public enum CullingMethod {
    EQUALS,
    ALWAYS,
    NEVER,
    SOLID;

    public static CullingMethod from(String id){
        return switch (id){
            case "solid"->SOLID;
            case "equals"->EQUALS;
            case "never"->NEVER;
            case "always"->ALWAYS;
            default -> throw new IllegalArgumentException("unknown value");
        };
    }
}
