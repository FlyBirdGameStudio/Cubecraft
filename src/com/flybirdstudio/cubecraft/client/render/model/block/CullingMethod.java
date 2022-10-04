package com.flybirdstudio.cubecraft.client.render.model.block;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.util.math.Vector3;

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
            case ALWAYS -> true;
            case NEVER -> false;
        };
    }
}
