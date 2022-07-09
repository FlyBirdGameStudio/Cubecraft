package com.sunrisestudio.cubecraft.event.entity;

import com.sunrisestudio.cubecraft.world.entity.Entity;

public record EntityChatEvent(Entity sender, String message) {
}
