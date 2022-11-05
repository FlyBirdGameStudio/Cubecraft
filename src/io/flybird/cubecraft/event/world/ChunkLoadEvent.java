package io.flybird.cubecraft.event.world;

import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.event.Event;

public record ChunkLoadEvent(Entity entity, long cx, long cy, long cz, ChunkLoadTicket ticket)implements Event {
}
