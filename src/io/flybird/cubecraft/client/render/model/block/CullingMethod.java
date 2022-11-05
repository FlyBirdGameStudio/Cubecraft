package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.util.math.Vector3;

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

    public static boolean shouldRender(IWorld world, long x, long y, long z, String id, EnumFacing absFacing, CullingMethod culling){
        Vector3<Long> pos=absFacing.findNear(x,y,z,1);
        return switch (culling){
            case EQUALS-> Objects.equals(world.getBlockState(pos.x(), pos.y(), pos.z()).getId(), id);
            case SOLID -> world.getBlockState(pos.x(), pos.y(), pos.z()).getBlock().isSolid();
            case ALWAYS -> false;
            case NEVER -> true;
        };
    }
}
