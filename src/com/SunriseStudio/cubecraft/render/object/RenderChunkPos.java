package com.SunriseStudio.cubecraft.render.object;

import com.SunriseStudio.cubecraft.render.sort.DistanceComparable;
import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyComparable;
import com.SunriseStudio.cubecraft.world.entity._Entity;

public record RenderChunkPos(long x, long y, long z) implements
        KeyComparable<RenderChunkPos>, DistanceComparable,Comparable<RenderChunkPos> {
    @Override
    public boolean compare(RenderChunkPos another) {
        return this.x == another.x && this.y == another.y && this.z == another.z;
    }

    @Override
    public int compareTo(RenderChunkPos o) {
        return 0;
    }

    @Override
    public double distanceTo(_Entity target) {
        double x = Math.abs(target.x - this.x * 16);
        double y = Math.abs(target.y - this.y * 16);
        double z = Math.abs(target.z - this.z * 16);
        return x*y*z;
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.x) + this.y + this.z).hashCode();
    }
}
