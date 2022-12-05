package io.flybird.cubecraft.world;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.world.chunk.*;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.EntityLocation;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.cubecraft.world.worldGen.ChunkProvider;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.math.MathHelper;
import io.flybird.util.math.Vector3;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class ServerWorld extends IWorld{
    public final String id;
    private final GameSetting setting;
    private final Level level;

    public ServerWorld(String id,Level level, LevelInfo levelInfo, GameSetting setting) {
        super(id,levelInfo);
        this.id = id;
        this.setting = setting;
        this.level=level;
    }

    //todo:test
    //load chunk
    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        if (getChunk(p) == null) {
            this.chunks.add(new ChunkProvider(ServerWorld.this).loadChunk(p));
        }else{
            getChunk(p).addTicket(ticket);
        }
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

        //keep spawn location
        EntityLocation loc=this.level.getSpawnPoint("__LOAD");
        if(Objects.equals(loc.getDim(), this.getID())) {
            this.loadChunkRange((long) (loc.getX() / 16), (long) (loc.getY() / 16), (long) (loc.getZ() / 16), 2, new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 20));
        }

        processChunkLoad();
        processEntity();
        processTickSchedule();
    }

    public void processChunkLoad() {
        Iterator<Chunk> it = this.chunks.map.values().iterator();
        try {
            while (it.hasNext()) {
                Chunk chunk = it.next();
                chunk.tick();
                if (!chunk.task.shouldLoad()) {
                    it.remove();
                }
            }
        } catch (ConcurrentModificationException ignored) {
            //remove has the lowest priority,so ignore this exception.
        }
    }

    public void processEntity() {
        Iterator<Entity> it2 = this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e instanceof Player) {
                e.tick();
                this.loadChunkRange((long) (e.x) / 16, (long) (e.y) / Chunk.HEIGHT, (long) (e.z) / 16, setting.getValueAsInt("server.world.simulation_distance",3), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING,10));
            } else {
                Chunk c = this.getChunk(new ChunkPos((long) (e.x) / 16, (long) (e.y) / Chunk.HEIGHT, (long) (e.z) / 16));
                if (c.task.shouldProcess(ChunkLoadTaskType.BLOCK_ENTITY_PROCESS)) {
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
            Chunk c=getChunk(
                    MathHelper.getRelativePosInChunk(key.x(),Chunk.WIDTH),
                    MathHelper.getRelativePosInChunk(key.y(),Chunk.HEIGHT),
                    MathHelper.getRelativePosInChunk(key.z(),Chunk.WIDTH));
            if (item > 0||c==null||!c.task.shouldProcess(ChunkLoadTaskType.INBOUND_BLOCK_UPDATE_PROCESS)) {
                this.scheduledTickEvents.put(key, item - 1);
            } else {
                getBlockState(key.x(), key.y(), key.z()).getBlock().onBlockUpdate(this, key.x(), key.y(), key.z());
                this.scheduledTickEvents.remove(key);
            }
        });
    }
}
