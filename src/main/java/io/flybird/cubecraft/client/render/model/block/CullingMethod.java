package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.util.container.Vector3;

import java.util.Objects;

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
