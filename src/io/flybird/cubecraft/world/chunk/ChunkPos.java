package io.flybird.cubecraft.world.chunk;

import io.flybird.util.container.keyMap.KeyComparable;
import io.flybird.util.math.MathHelper;

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
                MathHelper.getChunkPos(y,256),
                MathHelper.getChunkPos(z,16)
        );
    }

    public long toWorldPosX(int offset){
        return x*16+offset;
    }
    public long toWorldPosY(int offset){
        return y*256+offset;
    }
    public long toWorldPosZ(int offset){
        return z*16+offset;
    }
}
