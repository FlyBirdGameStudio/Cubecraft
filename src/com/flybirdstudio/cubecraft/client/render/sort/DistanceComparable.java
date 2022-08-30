package com.flybirdstudio.cubecraft.client.render.sort;

import com.flybirdstudio.cubecraft.world.entity.Entity;

public interface DistanceComparable {
    double distanceTo(Entity e);
}
