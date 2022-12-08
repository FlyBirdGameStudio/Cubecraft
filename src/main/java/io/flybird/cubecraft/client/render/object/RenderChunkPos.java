package io.flybird.cubecraft.client.render.object;

import io.flybird.cubecraft.client.render.sort.DistanceComparable;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.container.keyMap.Key;
import io.flybird.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public record RenderChunkPos(long x, long y, long z) implements
        Key<RenderChunkPos>, DistanceComparable,Comparable<RenderChunkPos> {

    @Override
    public int compareTo(@NotNull RenderChunkPos o) {
        return 0;
    }


    @Override
    public double distanceTo(Entity target){
        double x = MathHelper.clamp(Math.abs(target.x - this.x * 16),Double.MAX_VALUE,1);
        double y = MathHelper.clamp(Math.abs(target.y - this.y * 16),Double.MAX_VALUE,1);
        double z = MathHelper.clamp(Math.abs(target.z - this.z * 16),Double.MAX_VALUE,1);
        return x*y*z;
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.x) + this.y + this.z).hashCode();
    }

    public Vector3d clipToWorldPosition() {
        return new Vector3d(x*16,y*16,z*16);
    }
}
