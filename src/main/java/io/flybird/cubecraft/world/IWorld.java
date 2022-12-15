package io.flybird.cubecraft.world;

import io.flybird.cubecraft.internal.type.BlockType;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.container.keymap.KeyMap;
import io.flybird.util.event.EventBus;
import io.flybird.util.event.CachedEventBus;
import io.flybird.util.math.AABB;
import io.flybird.util.math.HitBox;
import io.flybird.util.math.MathHelper;
import io.flybird.util.container.Vector3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.*;
import java.util.function.Predicate;

public class IWorld {
    private final EventBus eventBus = new CachedEventBus();
    public final HashMap<Vector3<Long>, Integer> scheduledTickEvents = new HashMap<>();//event,remaining time
    public final KeyMap<ChunkPos, Chunk> chunks = new KeyMap<>();//position,chunk
    public final HashMap<String, Entity> entities = new HashMap<>();//uuid,entity
    private final LevelInfo levelInfo;
    private long time;
    private final String id;

    public IWorld(String id, LevelInfo levelInfo) {
        this.id = id;
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
                    AABB[] collisionBoxes = this.getBlockState(i, j, k).getCollisionBox();
                    if (collisionBoxes != null) {
                        result.addAll(List.of(collisionBoxes));
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

    public boolean isFree(AABB[] collisionBox) {
        for (AABB aabb : collisionBox) {
            for (AABB aabb2 : getCollisionBox(aabb)) {
                if (aabb.intersects(aabb2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<HitBox<Entity,IWorld>> getSelectionBox(Entity entity, Vector3d from, Vector3d dest) {
        ArrayList<HitBox<Entity,IWorld>> result = new ArrayList<>();

        for (long x = (long) Math.min(from.x, dest.x) - 2; x < Math.max(from.x, dest.x) + 2; x++) {
            for (long y = (long) Math.min(from.y, dest.y) - 2; y < Math.max(from.y, dest.y + 2) + 2; y++) {
                for (long z = (long) Math.min(from.z, dest.z) - 2; z < Math.max(from.z, dest.z) + 2; z++) {
                    result.addAll(List.of(getBlockState(x, y, z).getSelectionBox()));
                }
            }
        }

        for (Entity e : this.entities.values()) {
            if (
                    Math.abs(e.x - from.x) < e.getReachDistance() + 1 &&
                            Math.abs(e.y - from.y) < e.getReachDistance() + 1 &&
                            Math.abs(e.z - from.z) < e.getReachDistance() + 1 &&
                            entity != e
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }
        return result;
    }


    //info
    public String getID() {
        return this.id;
    }

    public long getSeed() {
        return 0;
    }

    public long getTime() {
        return this.time;
    }


    public WorldInfo getWorldInfo() {
        return new WorldInfo(0x81BDE9, 0x0A1772, 0xDCE9F5, 0xFFFFFF);
    }

    public KeyMap<ChunkPos, Chunk> getChunkCache() {
        return this.chunks;
    }


    //entity
    public void spawnEntity(String id, double x, double y, double z) {
        Entity e = Registries.ENTITY.create(id, this);
        e.setPos(x, y, z);
        this.addEntity(e);
    }

    public void addEntity(Entity e) {
        e.setWorld(IWorld.this);
        this.entities.put(e.getUID(), e);
        this.loadChunk((long) e.x / Chunk.WIDTH, (long) (e.y / Chunk.HEIGHT), (long) (e.z / Chunk.WIDTH), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
    }

    public Collection<Entity> getAllEntities() {
        return this.entities.values();
    }

    @Nullable
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
    public BlockState getBlockState(Vector3<Long> vec) {
        return getBlockState(vec.x(), vec.y(), vec.z());
    }

    public BlockState getBlockState(long x, long y, long z) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        Chunk c = getChunk(chunkPos);
        if (c == null) {
            return Registries.BLOCK.get(BlockType.AIR).defaultState(x, y, z);
        }

        return c.getBlockState(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH)
        ).setX(x).setY(y).setZ(z);
    }

    //load chunk
    public Chunk getChunk(ChunkPos p) {
        return this.chunks.get(p);
    }

    public Chunk getChunk(long cx, long cy, long cz) {
        return this.getChunk(new ChunkPos(cx, cy, cz));
    }

    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
    }

    public void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) {
        this.loadChunk(new ChunkPos(cx, cy, cz), chunkLoadTicket);
    }

    public void loadChunkRange(long centerCX, long centerCY, long centerCZ, int range, ChunkLoadTicket ticket) {
        for (long x = centerCX - range; x <= centerCX + range; x++) {
            for (long y = centerCY - 1; y <= centerCY + 1; y++) {
                for (long z = centerCZ - range; z <= centerCZ + range; z++) {
                    loadChunk(x, y, z, ticket);
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

    //schedule tick
    public void setTick(long x, long y, long z) {
    }

    public void setNeighborTick(long x, long y, long z) {
        setTick(x, y - 1, z);
        setTick(x, y + 1, z);
        setTick(x - 1, y, z);
        setTick(x + 1, y, z);
        setTick(x, y, z - 1);
        setTick(x, y, z + 1);
    }

    public void setTickSchedule(long x, long y, long z, int time) {
    }

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

    public void setBlockState(long x, long y, long z, BlockState newState) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return;
        }
        getChunk(chunkPos).setBlockState(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH),
                newState
        );
        this.getEventBus().callEvent(new BlockChangeEvent(this, x, y, z, newState));
    }

    public void setChunk(Chunk chunk) {
        this.chunks.forceAdd(chunk);
    }

    public int getLight(long x, long y, long z) {
        if (getBlockState(x, y, z).getBlock().isSolid()) {
            return 48;
        } else {
            return 128;
        }
    }

    public double getTemperature(long x, long y, long z) {
        return getChunk(ChunkPos.fromWorldPos(x, y, z)).getTemperature(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH)
        );
    }

    public double getHumidity(long x, long y, long z) {
        return getChunk(ChunkPos.fromWorldPos(x, y, z)).getHumidity(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH)
        );
    }

    public void setTemperature(long x, long y, long z, double t) {
        getChunk(ChunkPos.fromWorldPos(x, y, z)).setTemperature(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH), t
        );
    }

    public void setHumidity(long x, long y, long z, double t) {
        getChunk(ChunkPos.fromWorldPos(x, y, z)).setHumidity(
                (int) MathHelper.getRelativePosInChunk(x, Chunk.WIDTH),
                (int) MathHelper.getRelativePosInChunk(y, Chunk.HEIGHT),
                (int) MathHelper.getRelativePosInChunk(z, Chunk.WIDTH), t
        );
    }

    public LevelInfo getLevelInfo() {
        return levelInfo;
    }

    public boolean isAllNearMatch(long x, long y, long z, Predicate<BlockState> predicate){
        BlockState[] states=new BlockState[]{
                getBlockState(x+1, y, z),
                getBlockState(x-1, y, z),
                getBlockState(x, y+1, z),
                getBlockState(x, y-1, z),
                getBlockState(x, y, z+1),
                getBlockState(x, y, z-1)
        };
        return Arrays.stream(states).allMatch(predicate);
    }

}