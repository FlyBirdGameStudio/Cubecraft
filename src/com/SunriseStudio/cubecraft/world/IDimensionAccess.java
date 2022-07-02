package com.SunriseStudio.cubecraft.world;

import com.SunriseStudio.cubecraft.util.math.HitBox;
import com.SunriseStudio.cubecraft.world.block.Block;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.chunk.Chunk;
import com.SunriseStudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.SunriseStudio.cubecraft.world.chunk.ChunkPos;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import com.SunriseStudio.cubecraft.util.math.AABB;

import java.util.ArrayList;
import java.util.Collection;

public interface IDimensionAccess {
    //--- chunk ---
    Chunk getChunk(ChunkPos p);
    Chunk getChunk(long cx, long cy, long cz);
    void loadChunk(ChunkPos p, ChunkLoadTicket ticket);
    void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket);
    void loadChunkRange(long centerCX, long centerCY, long centerCZ, byte range, int ticks);

    //--- block ---
    Block getBlock(long x, long y, long z);
    void setBlock(long x, long y, long z, String id, BlockFacing facing);
    void setBlockNoUpdate(long x, long y, long z, String id, BlockFacing up);

    //--- physic ---
    ArrayList<AABB> getCollisionBox(AABB box);
    ArrayList<HitBox> getSelectionBox(Entity from);

    //--- entity ---
    Collection<Entity> getAllEntities();
    Entity getEntity(String uid);
    void spawnEntity(String id,double x,double y,double z);

    //meta
    String getID();

    World getWorld();

    void tick();

}
