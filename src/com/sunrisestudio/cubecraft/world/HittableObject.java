package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.util.math.HitResult;

public interface HittableObject {
    void onHit(Entity from, IWorldAccess world, HitResult hr);
    void onInteract(Entity from, IWorldAccess world,HitResult hr);
}
