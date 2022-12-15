package io.flybird.cubecraft.world;

import io.flybird.cubecraft.client.Cubecraft;

import io.flybird.cubecraft.world.chunk.*;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.event.world.ChunkLoadEvent;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class ClientWorld extends IWorld {
    final Cubecraft client;

    //todo:add remote world id
    public ClientWorld(LevelInfo levelInfo, Cubecraft client, String name) {
        super(name,levelInfo);
        this.client = client;
    }

    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        this.getEventBus().callEvent(new ChunkLoadEvent(client.getPlayer(), p.x(), p.y(), p.z(), ticket));
    }


    @Override
    public void tick() {
        super.tick();
        Iterator<Entity> it2=this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e==this.client.getPlayer()) {
                e.tick();
                this.loadChunkRange((long) (e.x) / 16, (long) (e.y) / Chunk.HEIGHT, (long) (e.z) / 16, client.getGameSetting().getValueAsInt("client.world.simulation_distance",3), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING,10));
            } else {
                Chunk c = this.getChunk(new ChunkPos((long) (e.x) / 16, (long) (e.y) / Chunk.HEIGHT, (long) (e.z) / 16));
                if (!c.task.shouldProcess(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS)) {
                    it2.remove();
                }
            }
        }

        Iterator<Chunk> it = this.chunks.map.values().iterator();
        try {
            while (it.hasNext()) {
                Chunk chunk = it.next();
                chunk.tick();
                if (chunk.getKey().distanceToEntity(this.client.getPlayer())>3) {
                    it.remove();
                }
            }
        } catch (ConcurrentModificationException ignored) {
            //remove has the lowest priority,so ignore this exception.
        }
    }
}
