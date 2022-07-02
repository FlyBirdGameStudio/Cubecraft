package com.SunriseStudio.cubecraft.render.sort;

import com.SunriseStudio.cubecraft.world.entity._Entity;

public interface DistanceComparable {
    double distanceTo(_Entity e);
}
