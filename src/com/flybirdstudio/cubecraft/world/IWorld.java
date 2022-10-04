package com.flybirdstudio.cubecraft.world;

import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.util.container.CollectionUtil;
import com.flybirdstudio.util.container.HashMapSet;
import com.flybirdstudio.util.event.EventBus;
import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.util.math.HitBox;
import com.flybirdstudio.util.math.MathHelper;
import com.flybirdstudio.util.math.Vector3;
import org.joml.Vector3d;

import java.util.*;

public class IWorld {
    private final EventBus eventBus=new EventBus();
    public HashMap<Vector3<Long>, Integer> scheduledTickEvents = new HashMap<>();//event,remaining time
    public HashMapSet<ChunkPos, Chunk> chunks = new HashMapSet<>();//position,chunk
    public HashMap<String, Entity> entities = new HashMap<>();//uuid,entity
    private LevelInfo levelInfo;
    private long time;
    private ArrayList<WorldListener> listeners = new ArrayList<>();

    public IWorld(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    //physics
    public ArrayList<AABB> getCollisionBox(AABB box) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) box.x0 - 4; i < (long) box.x1 + 4; i++) {
            for (long j = (long) box.y0 - 4; j < (long) box.y1 + 4; j++) {
                for (long k = (long) box.z0 - 4; k < (long) box.z1 + 4; k++) {
                    AABB[] collisionBoxes = this.getBlockState(i, j, k).getCollisionBox(i, j, k);
                    if (collisionBoxes != null) {
                        result.addAll(CollectionUtil.pack(collisionBoxes));
                    }
                }
            }
        }
        for (Entity e : this.entities.values()) {
            result.add(e.collisionBox);
        }
        result.remove(box);
        return result;
    }

    public boolean isfree(AABB[] collisionBox) {
        return true;
    }

    public ArrayList<HitBox> getSelectionBox(Entity entity,Vector3d from, Vector3d dest) {
        ArrayList<HitBox> result = new ArrayList<>();

        for (long x = (long) Math.min(from.x, dest.x)-2; x < Math.max(from.x, dest.x)+2; x++) {
            for (long y = (long) Math.min(from.y, dest.y)-2; y < Math.max(from.y, dest.y+2)+2; y++) {
                for (long z = (long) Math.min(from.z, dest.z)-2; z < Math.max(from.z, dest.z)+2; z++) {
                    result.addAll(CollectionUtil.pack(getBlockState(x,y,z).getSelectionBox(x,y,z)));
                }
            }
        }

        for (Entity e : this.entities.values()) {
            if (
                    Math.abs(e.x - from.x) < e.getReachDistance() + 1 &&
                            Math.abs(e.y - from.y) < e.getReachDistance() + 1 &&
                            Math.abs(e.z - from.z) < e.getReachDistance() + 1 &&
                            entity!=e
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }
        return result;
    }


    //info
    public String getID() {
        return "cubecraft:overworld";
    }

    public Level getLevel() {
        return this.levelInfo.level();
    }

    public long getSeed() {
        return 0;
    }

    public long getTime() {
        return this.time;
    }

    public Vector3d getSpawnPos() {
        return new Vector3d(0, 35, 0);
    }

    public WorldInfo getWorldInfo() {
        return new WorldInfo(0x81BDE9, 0x0A1772, 0xDCE9F5, 0xFFFFFF);
    }

    public HashMapSet<ChunkPos, Chunk> getChunkCache() {
        return this.chunks;
    }


    //entity
    public void spawnEntity(String id, double x, double y, double z) {
        Entity e = Registry.getEntityMap().create(id, this);
        e.setPos(x, y, z);
        this.addEntity(e);
    }

    public void addEntity(Entity e) {
        if (!this.entities.containsKey(e.getUID())) {
            this.entities.put(e.getUID(), e);
            e.setWorld(IWorld.this);
            this.loadChunk((long) e.x / 16, (long) (e.y / 16), (long) (e.z / 16), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
        } else {
            throw new RuntimeException("conflict entity uuid:" + e.getUID());
        }
    }

    public Collection<Entity> getAllEntities() {
        return this.entities.values();
    }

    public Entity getEntity(String uid) {
        return this.entities.get(uid);
    }

    public void removeEntity(String uid) {
        this.entities.remove(uid);
    }

    public void removeEntity(Entity e) {
        this.entities.remove(e.getUID());
    }


    //block
    public BlockState getBlockState(Vector3<Long> vec){
        return getBlockState(vec.x(), vec.y(), vec.z());
    }

    public BlockState getBlockState(long x, long y, long z) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return new BlockState("cubecraft:air");
        }

        return getChunk(chunkPos).getBlockState(
                (int) MathHelper.getRelativePosInChunk(x, 16),
                (int) MathHelper.getRelativePosInChunk(y, 16),
                (int) MathHelper.getRelativePosInChunk(z, 16)
        );
    }

    public void setBlock(long x, long y, long z, String id, EnumFacing facing) {
        setBlockNoUpdate(x, y, z, id, facing);
        setTick(x, y, z);
        setNeighborTick(x, y, z);
    }

    public void setBlockNoUpdate(long x, long y, long z, String id, EnumFacing facing) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return;
        }
        getChunk(chunkPos).setBlock(
                (int) MathHelper.getRelativePosInChunk(x, 16),
                (int) MathHelper.getRelativePosInChunk(y, 16),
                (int) MathHelper.getRelativePosInChunk(z, 16),
                id,
                facing
        );

        for (WorldListener wl : listeners) {
            wl.blockChanged(x, y, z);
        }
    }


    //load chunk
    public Chunk getChunk(ChunkPos p) {
        return this.chunks.get(p);
    }

    public Chunk getChunk(long cx, long cy, long cz) {
        return this.getChunk(new ChunkPos(cx, cy, cz));
    }

    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {}

    public void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) {
        this.loadChunk(new ChunkPos(cx, cy, cz), chunkLoadTicket);
    }

    public void loadChunkRange(long centerCX, long centerCY, long centerCZ, int range, int ticks) {
        for (long x = centerCX - range; x <= centerCX + range; x++) {
            for (long y = centerCY - range; y <= centerCY + range; y++) {
                for (long z = centerCZ - range; z <= centerCZ + range; z++) {
                    loadChunk(x, y, z, ChunkLoadTicket.fromDistance(range, ticks));
                }
            }
        }
    }

    public void loadChunkAndNear(long x, long y, long z, ChunkLoadTicket ticket) {
        loadChunkIfNull(x, y, z, ticket);
        loadChunkIfNull(x - 1, y, z, ticket);
        loadChunkIfNull(x + 1, y, z, ticket);
        loadChunkIfNull(x, y - 1, z, ticket);
        loadChunkIfNull(x, y + 1, z, ticket);
        loadChunkIfNull(x, y, z - 1, ticket);
        loadChunkIfNull(x, y, z + 1, ticket);

    }

    public void loadChunkIfNull(long x, long y, long z, ChunkLoadTicket ticket) {
        if (getChunk(x, y, z) == null) {
            loadChunk(new ChunkPos(x, y, z), ticket);
        }
    }


    //listener
    public void addListener(WorldListener worldListener) {
        this.listeners.add(worldListener);
    }

    public void removeListener(WorldListener worldListener) {
        this.listeners.remove(worldListener);
    }


    //schedule tick
    public void setTick(long x, long y, long z) {}

    public void setNeighborTick(long x, long y, long z) {
        setTick(x, y - 1, z);
        setTick(x, y + 1, z);
        setTick(x - 1, y, z);
        setTick(x + 1, y, z);
        setTick(x, y, z - 1);
        setTick(x, y, z + 1);
    }

    public void setTickSchedule(long x, long y, long z, int time) {}

    private void setNeighborScheduleTick(long x, long y, long z, int time) {
        setTickSchedule(x, y - 1, z, time);
        setTickSchedule(x, y + 1, z, time);
        setTickSchedule(x - 1, y, z, time);
        setTickSchedule(x + 1, y, z, time);
        setTickSchedule(x, y, z - 1, time);
        setTickSchedule(x, y, z + 1, time);
    }


    //tick
    public void tick() {
        time++;
    }

    public void setBlockState(long x,long y,long z,BlockState newState) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return;
        }
        getChunk(chunkPos).setBlockState(
                (int) MathHelper.getRelativePosInChunk(x, 16),
                (int) MathHelper.getRelativePosInChunk(y, 16),
                (int) MathHelper.getRelativePosInChunk(z, 16),
                newState
        );
    }

    public void setChunk(Chunk chunk) {
        this.chunks.forceAdd(chunk);
    }
}
