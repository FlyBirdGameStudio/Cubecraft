package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.util.math.HitResult;
import com.sunrisestudio.cubecraft.world.entity.Entity;

public interface HittableObject {
    void onHit(Entity from, World world, HitResult hr);
    void onInteract(Entity from, World world, HitResult hr);
}
