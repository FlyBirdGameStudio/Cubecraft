package com.flybirdstudio.cubecraft.world;

import com.flybirdstudio.util.math.HitResult;
import com.flybirdstudio.cubecraft.world.entity.Entity;

public interface HittableObject {
    void onHit(Entity from, IWorld world, HitResult hr);
    void onInteract(Entity from, IWorld world, HitResult hr);
}
