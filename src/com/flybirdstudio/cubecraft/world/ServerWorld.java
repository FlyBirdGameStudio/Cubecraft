package com.flybirdstudio.cubecraft.world;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.cubecraft.world.worldGen.WorldProvider;
import com.flybirdstudio.util.container.CollectionUtil;
import com.flybirdstudio.util.math.Vector3;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class ServerWorld extends IWorld{
    public ServerWorld(LevelInfo levelInfo) {
        super(levelInfo);
    }

    //load chunk
    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        if (getChunk(p) == null) {
            chunks.add(WorldProvider.getProvider(this).loadChunk(p));
        }
        getChunk(p).addTicket(ticket);
    }


    //schedule tick
    @Override
    public void setTick(long x, long y, long z) {
        setTickSchedule(x, y, z, -1);
    }

    @Override
    public void setTickSchedule(long x, long y, long z, int time) {
        this.scheduledTickEvents.put(new Vector3<>(x, y, z), time);
    }

    //tick
    @Override
    public void tick() {
        super.tick();
        processChunkLoad();
        processEntity();
        processTickSchedule();
    }

    public void processChunkLoad() {
        Iterator<Chunk> it = this.chunks.map.values().iterator();
        try {
            while (it.hasNext()) {
                Chunk chunk = it.next();
                chunk.ticket.tickTime();
                if (chunk.ticket.getTime() <= 0) {
                    it.remove();
                }
            }
        } catch (ConcurrentModificationException fuckyouasync) {
            //remove has the lowest priority,so ignore this exception.
        }
    }

    public void processEntity() {
        Iterator<Entity> it2 = this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e instanceof Player) {
                e.tick();
                this.loadChunkRange((long) (e.x) / 16, (long) (e.y) / 16, (long) (e.z) / 16, GameSetting.instance.getValueAsInt("client.world.simulationDistance", 3), 50);
            } else {
                Chunk c = this.getChunk(new ChunkPos((long) (e.x) / 16, (long) (e.y) / 16, (long) (e.z) / 16));
                if (c.ticket.getTime() > 0 && c.ticket.getChunkLoadLevel().containsLevel(ChunkLoadLevel.Entity_TICKING)) {
                    e.tick();
                } else {
                    it2.remove();
                }
            }
        }
    }

    public void processTickSchedule() {
        HashMap<Vector3<Long>, Integer> times = (HashMap<Vector3<Long>, Integer>) this.scheduledTickEvents.clone();
        this.scheduledTickEvents.clear();
        CollectionUtil.iterateMap(times, (key, item) -> {
            if (item > 0) {
                this.scheduledTickEvents.put(key, item - 1);
            } else {
                getBlockState(key.x(), key.y(), key.z()).getBlock().onBlockUpdate(this, key.x(), key.y(), key.z());
                this.scheduledTickEvents.remove(key);
            }
        });
    }
}
