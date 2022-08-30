package com.flybirdstudio.cubecraft.event.entity;

import com.flybirdstudio.cubecraft.world.entity.Entity;

public record EntityChatEvent(Entity sender, String message) {
}
