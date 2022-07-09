package com.sunrisestudio.cubecraft.event.entity;

import com.sunrisestudio.cubecraft.world.entity.Entity;

public record EntityInteractEntityEvent(Entity From, Entity to) {
}
