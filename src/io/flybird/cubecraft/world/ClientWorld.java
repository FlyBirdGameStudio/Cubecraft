package io.flybird.cubecraft.world;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.event.world.ChunkLoadEvent;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.chunk.ChunkPos;

public class ClientWorld extends IWorld {
    final Cubecraft client;

    //todo:add remote world id
    public ClientWorld(LevelInfo levelInfo, Cubecraft client) {
        super("__client",levelInfo);
        this.client = client;
    }

    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        this.getEventBus().callEvent(new ChunkLoadEvent(client.getPlayer(), p.x(), p.y(), p.z(), ticket));
    }
}
