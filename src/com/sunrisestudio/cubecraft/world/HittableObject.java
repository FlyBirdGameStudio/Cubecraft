package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.world.entity.Entity;

public interface HittableObject {
    void onHit(Entity from, IWorldAccess world,long bx,long by,long bz);
}
