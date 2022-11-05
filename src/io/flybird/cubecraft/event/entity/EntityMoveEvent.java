package io.flybird.cubecraft.event.entity;

import io.flybird.cubecraft.event.CancelCallback;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.EntityLocation;

public record EntityMoveEvent(
        Entity e,
        EntityLocation oldLocation,
        EntityLocation newLocation,
        CancelCallback cancelCallback
) {}
