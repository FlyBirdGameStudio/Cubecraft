package com.sunrisestudio.cubecraft.render.sort;

import com.sunrisestudio.cubecraft.world.entity._Entity;

public interface DistanceComparable {
    double distanceTo(_Entity e);
}
