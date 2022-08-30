package com.flybirdstudio.cubecraft.event.entity;

import com.flybirdstudio.cubecraft.event.CancelCallback;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.entity.EntityLocation;

public record EntityMoveEvent(
        Entity e,
        EntityLocation oldLocation,
        EntityLocation newLocation,
        CancelCallback cancelCallback
) {}
