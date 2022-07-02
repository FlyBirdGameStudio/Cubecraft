package com.SunriseStudio.cubecraft.world;

import com.SunriseStudio.cubecraft.world.entity.Entity;

public interface HittableObject {

    void onHit(Entity from, IDimensionAccess world);
}
