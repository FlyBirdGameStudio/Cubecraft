package com.SunriseStudio.cubecraft.world;

import com.SunriseStudio.cubecraft.util.collections.keyMap.KeyMap;
import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.util.math.HitBox;
import com.SunriseStudio.cubecraft.world.block.Block;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.chunk.Chunk;
import com.SunriseStudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.SunriseStudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.SunriseStudio.cubecraft.world.chunk.ChunkPos;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import com.SunriseStudio.cubecraft.world.entity.EntityMap;
import com.SunriseStudio.cubecraft.world.entity.humanoid.Player;

import java.util.*;

public class Dimension implements IDimensionAccess {
    public KeyMap<ChunkPos, Chunk> chunks;
    public HashMap<String, Entity> entities;
    private World world;

    //chunks
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
            this.chunks.elements.add(null);//todo:link to world provider
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


    //block
    @Override
    public Block getBlock(long x, long y, long z) {
        return getChunk(new ChunkPos(x / 16, y / 16, z / 16)).getBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16));
    }

    @Override
    public void setBlock(long x, long y, long z, String id, BlockFacing facing) {
        getChunk(new ChunkPos(x / 16, y / 16, z / 16)).setBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16), id, facing);
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
        getChunk(new ChunkPos(x / 16, y / 16, z / 16)).setBlock((int) (x - x / 16 * 16), (int) y, (int) (z - z / 16 * 16), id, facing);
    }


    //entity
    @Override
    public void spawnEntity(String id, double x, double y, double z) {
        Entity e = EntityMap.getEntity(this, id);
        assert e != null;
        if (this.entities.containsKey(e.getUID())) {
            e.setPos(x, y, z);
            this.entities.put(e.getUID(), e);
            loadChunk((long) (e.x / 16), (long) (e.y / 16), (long) (e.z / 16), new ChunkLoadTicket(ChunkLoadLevel.Entity_TICKING, 256));
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


    //physics
    @Override
    public ArrayList<AABB> getCollisionBox(AABB box) {
        ArrayList<AABB> result = new ArrayList<>();
        for (long i = (long) box.x0 - 1; i < (long) box.x1 + 1; i++) {
            for (int j = (int) box.y0 - 1; j < (int) box.y1 + 1; j++) {
                for (long k = (long) box.z0 - 1; k < (long) box.z1 + 1; k++) {
                    if (this.getBlock(i, j, k).getMaterial().getCollisionBox(i, j, k) != null) {
                        result.addAll(List.of(getBlock(i, j, k).getMaterial().getCollisionBox(i, j, k)));
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
                    if (this.getBlock(i, j, k).getMaterial().getSelectionBox(i, j, k) != null) {
                        result.addAll(List.of(getBlock(i, j, k).getMaterial().getSelectionBox(i, j, k)));
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
    public World getWorld() {
        return this.world;
    }

    @Override
    public void tick() {
        Iterator<Chunk> it = this.chunks.elements.iterator();
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
                Chunk c = getChunk(new ChunkPos((long) (e.x / 16), (long) (e.y / 16), (long) (e.z / 16)));
                if (c.ticket.getTime() > 0 && c.ticket.getChunkLoadLevel().containsLevel(ChunkLoadLevel.Entity_TICKING)) {
                    e.tick();
                } else {
                    it2.remove();
                }
            }
        }
    }
}
