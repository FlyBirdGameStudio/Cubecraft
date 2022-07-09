package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.util.container.HashMapSet;
import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.cubecraft.world.access.IBlockAccess;
import com.sunrisestudio.cubecraft.world.access.IChunkAccess;
import com.sunrisestudio.cubecraft.world.access.IEntityAccess;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.Block;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.EntityMap;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.cubecraft.world.worldGen.WorldProvider;

import java.util.*;

public class World implements IWorldAccess {
    public HashMapSet<ChunkPos, Chunk> chunks= new HashMapSet<>();
    public HashMap<String, Entity> entities=new HashMap<>();
    private Level level;
    private String id;

    //physics
    @Override
    public ArrayList<AABB> getCollisionBox(AABB box) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) box.x0 - 1; i < (long) box.x1 + 1; i++) {
            for (int j = (int) box.y0 - 1; j < (int) box.y1 + 1; j++) {
                for (long k = (long) box.z0 - 1; k < (long) box.z1 + 1; k++) {
                    if (this.getBlockAccess().getBlock(i, j, k).getMaterial().getCollisionBox(i, j, k) != null) {
                        result.addAll(List.of(this.getBlockAccess().getBlock(i, j, k).getMaterial().getCollisionBox(i, j, k)));
                    }
                }
            }
        }
        for (Entity e : this.entities.values()) {
            if (
                    (Math.abs(e.x - box.x0) < e.getReachDistance() + 1 || Math.abs(e.x - box.x1) < e.getReachDistance() + 1) &&
                            (Math.abs(e.y - box.y0) < e.getReachDistance() + 1 || Math.abs(e.y - box.y1) < e.getReachDistance() + 1) &&
                            (Math.abs(e.z - box.z0) < e.getReachDistance() + 1 || Math.abs(e.z - box.z1) < e.getReachDistance() + 1)
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }
        return result;
    }

    @Override
    public ArrayList<HitBox> getSelectionBox(Entity from) {
        ArrayList<HitBox> result = new ArrayList<>();
        double dist = from.getReachDistance();
        for (long i = (long) (from.x - dist); i < from.x + dist; i++) {
            for (long j = (long) (from.y - dist); j < from.y + dist; j++) {
                for (long k = (long) (from.z - dist); k < from.z + dist; k++) {
                    if (this.getBlockAccess().getBlock(i, j, k).getMaterial().getSelectionBox(i, j, k) != null) {
                        result.addAll(List.of(this.getBlockAccess().getBlock(i, j, k).getMaterial().getSelectionBox(i, j, k)));
                    }
                }
            }
        }
        for (Entity e : this.entities.values()) {
            if (
                    Math.abs(e.x - from.x) < e.getReachDistance() + 1 &&
                            Math.abs(e.y - from.y) < e.getReachDistance() + 1 &&
                            Math.abs(e.z - from.z) < e.getReachDistance() + 1
            ) {
                result.addAll(List.of(e.getSelectionBoxes()));
            }
        }
        return result;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public void tick() {
        Iterator<Chunk> it = this.chunks.map.values().iterator();
        while (it.hasNext()) {
            Chunk chunk = it.next();
            chunk.ticket.tickTime();
            if (chunk.ticket.getTime() <= 0) {
                it.remove();
            }
        }
        Iterator<Entity> it2 = this.entities.values().iterator();
        while (it2.hasNext()) {
            Entity e = it2.next();
            if (e instanceof Player) {
                e.tick();
            }else{
                Chunk c = this.getChunkAccess().getChunk(new ChunkPos((long) (e.x / 16), (long) (e.y / 16), (long) (e.z / 16)));
                if (c.ticket.getTime() > 0 && c.ticket.getChunkLoadLevel().containsLevel(ChunkLoadLevel.Entity_TICKING)) {
                    e.tick();
                } else {
                    it2.remove();
                }
            }
        }
    }


    //access

    @Override
    public IBlockAccess getBlockAccess() {
        return new IBlockAccess() {
            @Override
            public Block getBlock(long x, long y, long z) {
                return getChunkAccess().getChunk(new ChunkPos(x / 16, y / 16, z / 16)).getBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16));
            }

            @Override
            public void setBlock(long x, long y, long z, String id, BlockFacing facing) {
                getChunkAccess().
                        getChunk(new ChunkPos(x / 16, y / 16, z / 16)).
                        setBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16), id, facing);
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
                getChunkAccess().
                        getChunk(new ChunkPos(x / 16, y / 16, z / 16)).
                        setBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16), id, facing);
            }

            @Override
            public void setUpdate(long x, long y, long z){
                getBlock(x, y, z).setTicking(true);
            }
        };
    }

    @Override
    public IChunkAccess getChunkAccess() {
        return new IChunkAccess() {
            @Override
            public Chunk getChunk(ChunkPos p) {
                return World.this.chunks.get(p);
            }

            @Override
            public Chunk getChunk(long cx, long cy, long cz) {
                return this.getChunk(new ChunkPos(cx, cy, cz));
            }

            @Override
            public void loadChunk(ChunkPos p, ChunkLoadTicket ticket) {
                if (getChunk(p) == null) {
                    World.this.chunks.add(WorldProvider.getProvider(World.this.getID()).getChunk(p));
                }
                this.getChunk(p).ticket = ticket;
            }

            @Override
            public void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) {
                this.loadChunk(new ChunkPos(cx, cy, cz), chunkLoadTicket);
            }

            @Override
            public void loadChunkRange(long centerCX, long centerCY, long centerCZ, byte range, int ticks) {
                for (long x = centerCX - range; x < centerCX + range; x++) {
                    for (long y = centerCX - range; y < centerCX + range; y++) {
                        for (long z = centerCX - range; z < centerCX + range; z++) {
                            loadChunk(x, y, z, ChunkLoadTicket.fromDistance(range, ticks));
                        }
                    }
                }
            }
        };
    }

    @Override
    public IEntityAccess getEntityAccess() {
        return new IEntityAccess() {
            //entity
            @Override
            public void spawnEntity(String id, double x, double y, double z) {
                Entity e = EntityMap.getInstance().getEntity(World.this, id);
                assert e != null;
                e.setPos(x, y, z);
                this.addEntity(e);
            }

            @Override
            public void addEntity(Entity e) {
                if (World.this.entities.containsKey(e.getUID())) {
                    World.this.entities.put(e.getUID(), e);
                    World.this.getChunkAccess().loadChunk((long) (e.x / 16), (long) (e.y / 16), (long) (e.z / 16), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
                }
            }

            @Override
            public Collection<Entity> getAllEntities() {
                return World.this.entities.values();
            }

            @Override
            public Entity getEntity(String uid) {
                return World.this.entities.get(uid);
            }

            @Override
            public void removeEntity(String uid){
                World.this.entities.remove(uid);
            }

            @Override
            public void removeEntity(Entity e){
                World.this.entities.remove(e.getUID());
            }
        };
    }
}
