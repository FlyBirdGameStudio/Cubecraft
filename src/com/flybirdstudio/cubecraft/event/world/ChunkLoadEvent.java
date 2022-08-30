package com.flybirdstudio.cubecraft.event.world;

import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.util.event.Event;

public record ChunkLoadEvent(Entity entity, long cx, long cy, long cz, ChunkLoadTicket ticket)implements Event {
}
