package com.flybirdstudio.cubecraft.world.chunk;

import com.flybirdstudio.util.container.keyMap.KeyComparable;
import com.flybirdstudio.util.math.MathHelper;

public record ChunkPos (long x, long y,long z) implements KeyComparable<ChunkPos> {

    @Override
    public boolean compare(ChunkPos another){
        return this.x==another.x&&this.y==another.y&&this.z==another.z;
    }

    @Override
    public String toString() {
        return x+"/"+y+"/"+z;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static ChunkPos fromWorldPos(long x, long y, long z){
        return new ChunkPos(
                MathHelper.getChunkPos(x,16),
                MathHelper.getChunkPos(y,16),
                MathHelper.getChunkPos(z,16)
        );
    }

    public long toWorldPosX(int offset){
        return x*16+offset;
    }
    public long toWorldPosY(int offset){
        return y*16+offset;
    }
    public long toWorldPosZ(int offset){
        return z*16+offset;
    }
}
