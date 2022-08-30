package com.flybirdstudio.cubecraft.event.entity;

import com.flybirdstudio.cubecraft.world.entity.Entity;

public record EntityAttackEvent(Entity from, Entity target) {
}
