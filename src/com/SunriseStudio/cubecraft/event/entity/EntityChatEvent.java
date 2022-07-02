package com.SunriseStudio.cubecraft.event.entity;

import com.SunriseStudio.cubecraft.world.entity.Entity;

public record EntityChatEvent(Entity sender, String message) {
}
