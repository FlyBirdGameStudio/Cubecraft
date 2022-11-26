package io.flybird.cubecraft.world;

import io.flybird.util.math.HitResult;
import io.flybird.cubecraft.world.entity.Entity;

public interface HittableObject {
    void onHit(Entity from, IWorld world, HitResult hr);
    void onInteract(Entity from, IWorld world, HitResult hr);
}
