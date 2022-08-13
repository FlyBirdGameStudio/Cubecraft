package com.sunrisestudio.cubecraft.event.entity;

import com.sunrisestudio.cubecraft.event.CancelCallback;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.EntityLocation;

public record EntityMoveEvent(
        Entity e,
        EntityLocation oldLocation,
        EntityLocation newLocation,
        CancelCallback cancelCallback
) {}
