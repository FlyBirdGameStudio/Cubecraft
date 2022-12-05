package io.flybird.cubecraft.world.chunk;

import io.flybird.cubecraft.world.entity.Entity;
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
                MathHelper.getChunkPos(x,Chunk.WIDTH),
                MathHelper.getChunkPos(y,Chunk.HEIGHT),
                MathHelper.getChunkPos(z,Chunk.WIDTH)
        );
    }

    public long toWorldPosX(int offset){
        return x*Chunk.WIDTH+offset;
    }
    public long toWorldPosY(int offset){
        return y*Chunk.HEIGHT+offset;
    }
    public long toWorldPosZ(int offset){
        return z*Chunk.WIDTH+offset;
    }

    public double distanceToEntity(Entity e) {
        return Math.max(Math.abs(x-e.x/16),Math.abs(z-e.z/16));
    }
}
