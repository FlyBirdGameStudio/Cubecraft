package com.SunriseStudio.cubecraft.world.chunk;

import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyComparable;

public record ChunkPos (long x, long y,long z) implements KeyComparable<ChunkPos> {

    @Override
    public boolean compare(ChunkPos another){
        return this.x==another.x&&this.y==another.y&&this.z==another.z;
    }
}
