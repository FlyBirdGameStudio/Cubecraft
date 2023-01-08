package io.flybird.cubecraft.client.render.sort;

import io.flybird.cubecraft.world.entity.Entity;

public interface DistanceComparable {
    double distanceTo(Entity e);
}
