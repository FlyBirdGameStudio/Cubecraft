package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.util.math.HitResult;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public interface HittableObject {
    void onHit(Entity from, com.sunrisestudio.cubecraft.world.IWorldAccess world, HitResult hr);
    void onInteract(Entity from, IWorldAccess world, HitResult hr);
}
