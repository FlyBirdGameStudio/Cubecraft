package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.world.worldGen.WorldProvider;
import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.util.container.HashMapSet;
import com.sunrisestudio.util.math.*;

import org.joml.Vector3d;

import java.util.*;

public class World implements IWorldAccess {
    public HashMapSet<ChunkPos, Chunk> chunks = new HashMapSet<>();
    public HashMap<String, Entity> entities = new HashMap<>();
    private  LevelInfo levelInfo;
    private long time;
    private ArrayList<WorldListener> listeners=new ArrayList<>();


    public World(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    //physics
    @Override
    public ArrayList<AABB> getCollisionBox(AABB box) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) box.x0 - 4; i < (long) box.x1 + 4; i++) {
            for (long j = (long) box.y0 - 4; j < (long) box.y1 + 4; j++) {
                for (long k = (long) box.z0 - 4; k < (long) box.z1 + 4; k++) {
                    AABB[] collisionBoxes = this.getBlock(i, j, k).getCollisionBox(i, j, k);
                    if (collisionBoxes != null) {
                        result.addAll(CollectionUtil.pack(collisionBoxes));
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<HitBox> getSelectionBox(Entity from) {
        ArrayList<HitBox> result = new ArrayList<>();
        for (long i = (long) ((long) from.x - from.getReachDistance()); i < (long) from.x + from.getReachDistance(); i++) {
            for (long j = (long) ((long) from.y - from.getReachDistance()); j < (long) from.y + from.getReachDistance(); j++) {
                for (long k = (long) ((long) from.z - from.getReachDistance()); k < (long) from.z + from.getReachDistance(); k++) {
                    HitBox[] collisionBoxes = this.getBlock(i, j, k).getSelectionBox(this, i, j, k);
                    if (collisionBoxes != null) {
                        result.addAll(CollectionUtil.pack(collisionBoxes));
                    }
                }
            }
        }
        /*
        for (Entity e : this.entities.values()) {
            if (
                    Math.abs(e.x - from.x) < e.getReachDistance() + 1 &&
                            Math.abs(e.y - from.y) < e.getReachDistance() + 1 &&
                            Math.abs(e.z - from.z) < e.getReachDistance() + 1
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }

         */
        return result;
    }

    @Override
    public String getID() {
        return "cubecraft:over_world";
    }

    @Override
    public Level getLevel() {
        return this.levelInfo.level();
    }

    @Override
    public long getSeed() {
        return 0;
    }

    @Override
    public void tick() {
        time++;
        Iterator<Chunk> it = this.chunks.map.values().iterator();
        try {
            while (it.hasNext()) {
                Chunk chunk = it.next();
                chunk.ticket.tickTime();
                if (chunk.ticket.getTime() <= 0) {
                    it.remove();
                }
            }
        }catch (ConcurrentModificationException fuckyouasync){
            //remove has the lowest priority,so ignore this exception.
        }
        Iterator<Entity> it2 = this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e instanceof Player) {
                e.tick();
                this.loadChunkRange((long) (e.x) / 16, (long) (e.y) / 16, (long) (e.z) / 16, GameSetting.instance.getValueAsInt("client.world.simulationDistance",3), 50);
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

    @Override
    public Chunk getChunk(ChunkPos p) {
        return this.chunks.get(p);
    }

    @Override
    public Chunk getChunk(long cx, long cy, long cz) {
        return this.getChunk(new ChunkPos(cx, cy, cz));
    }

    @Override
    public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
        if (getChunk(p) == null) {
            chunks.add(WorldProvider.getProvider(this).loadChunk(p));
        }
        getChunk(p).addTicket(ticket);
    }

    @Override
    public void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) {
        this.loadChunk(new ChunkPos(cx, cy, cz), chunkLoadTicket);
    }

    @Override
    public void loadChunkRange(long centerCX, long centerCY, long centerCZ, int range, int ticks) {
        for (long x = centerCX - range; x <= centerCX + range; x++) {
            for (long y = centerCY - range; y <= centerCY + range; y++) {
                for (long z = centerCZ - range; z <= centerCZ + range; z++) {
                    loadChunk(x, y, z, ChunkLoadTicket.fromDistance(range, ticks));
                }
            }
        }
    }

    @Override
    public long getTime() {
        return this.time;
    }

    public Vector3d getSpawnPos() {
        return new Vector3d(0, 35, 0);
    }

    @Override
    public void spawnEntity(String id, double x, double y, double z) {
        Entity e = Registry.getEntityMap().create(id,this);
        e.setPos(x, y, z);
        this.addEntity(e);
    }

    @Override
    public void addEntity(Entity e) {
        if (!this.entities.containsKey(e.getUID())) {
            this.entities.put(e.getUID(), e);
            e.setWorld(World.this);
            this.loadChunk((long) e.x / 16, (long) (e.y / 16), (long) (e.z / 16), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
        } else {
            throw new RuntimeException("conflict entity uuid:" + e.getUID());
        }
    }

    @Override
    public Collection<Entity> getAllEntities() {
        return this.entities.values();
    }

    @Override
    public Entity getEntity(String uid) {
        return this.entities.get(uid);
    }

    @Override
    public void removeEntity(String uid) {
        this.entities.remove(uid);
    }

    @Override
    public void removeEntity(Entity e) {
        this.entities.remove(e.getUID());
    }

    @Override
    public BlockState getBlock(long x, long y, long z) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return new BlockState("cubecraft:air");
        }

        return getChunk(chunkPos).getBlock(
                (int)MathHelper.getRelativePosInChunk(x,16),
                (int)MathHelper.getRelativePosInChunk(y,16),
                (int)MathHelper.getRelativePosInChunk(z,16)
        );
    }

    @Override
    public void setBlock(long x, long y, long z, String id, BlockFacing facing) {
        setBlockNoUpdate(x, y, z, id, facing);
        getBlock(x, y, z).setTicking(true);
        getBlock(x + 1, y, z).setTicking(true);
        getBlock(x - 1, y, z).setTicking(true);
        getBlock(x, y + 1, z).setTicking(true);
        getBlock(x, y - 1, z).setTicking(true);
        getBlock(x, y, z + 1).setTicking(true);
        getBlock(x, y, z - 1).setTicking(true);
    }

    @Override
    public void setBlockNoUpdate(long x, long y, long z, String id, BlockFacing facing) {
        ChunkPos chunkPos = ChunkPos.fromWorldPos(x, y, z);
        if (getChunk(chunkPos) == null) {
            return;
        }
        getChunk(chunkPos).setBlock(
                (int)MathHelper.getRelativePosInChunk(x,16),
                (int)MathHelper.getRelativePosInChunk(y,16),
                (int)MathHelper.getRelativePosInChunk(z,16),
                id,
                facing
        );

        for(WorldListener wl:listeners){
            wl.blockChanged(x,y,z);
        }
    }

    @Override
    public void setUpdate(long x, long y, long z) {
        getBlock(x, y, z).setTicking(true);
    }

    @Override
    public WorldInfo getWorldInfo() {
        return new WorldInfo(0x81BDE9, 0x0A1772, 0xDCE9F5, 0xFFFFFF);
    }

    @Override
    public HashMapSet<ChunkPos, Chunk> getChunkCache() {
        return this.chunks;
    }

    @Override
    public void loadChunkAndNear(long x, long y, long z, ChunkLoadTicket ticket) {
        loadChunkIfNull(x,y,z,ticket);
        loadChunkIfNull(x-1,y,z,ticket);
        loadChunkIfNull(x+1,y,z,ticket);
        loadChunkIfNull(x,y-1,z,ticket);
        loadChunkIfNull(x,y+1,z,ticket);
        loadChunkIfNull(x,y,z-1,ticket);
        loadChunkIfNull(x,y,z+1,ticket);

    }

    public void loadChunkIfNull(long x,long y,long z,ChunkLoadTicket ticket){
        if (getChunk(x, y, z) == null) {
            loadChunk(new ChunkPos(x, y, z), ticket);
        }
    }

    public void addListener(WorldListener worldListener){
        this.listeners.add(worldListener);
    }

    public void removeListener(WorldListener worldListener){
        this.listeners.remove(worldListener);
    }
}
