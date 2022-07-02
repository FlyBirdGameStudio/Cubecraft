package com.SunriseStudio.cubecraft.event.entity;

import com.SunriseStudio.cubecraft.event.CancelCallback;
import com.SunriseStudio.cubecraft.event.Event;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import com.SunriseStudio.cubecraft.world.entity.EntityLocation;
import org.joml.Vector3d;

public record EntityMoveEvent(
        Entity e,
        EntityLocation oldLocation,
        EntityLocation newLocation,
        CancelCallback cancelCallback
) {}
