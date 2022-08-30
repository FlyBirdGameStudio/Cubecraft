package com.flybirdstudio.cubecraft.event.entity;

import com.flybirdstudio.cubecraft.world.entity.Entity;

public record EntityInteractEntityEvent(Entity From, Entity to) {
}
