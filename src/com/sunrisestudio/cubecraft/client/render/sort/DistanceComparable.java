package com.sunrisestudio.cubecraft.client.render.sort;

import com.sunrisestudio.cubecraft.world.entity.Entity;

public interface DistanceComparable {
    double distanceTo(Entity e);
}
