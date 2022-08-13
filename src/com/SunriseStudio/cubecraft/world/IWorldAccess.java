package com.sunrisestudio.cubecraft.world;

import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.util.container.HashMapSet;
import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.util.math.AABB;

import java.util.ArrayList;
import java.util.Collection;

public interface IWorldAccess{

    //--- physic ---
    ArrayList<AABB> getCollisionBox(AABB box);
    ArrayList<HitBox> getSelectionBox(Entity from);

    //meta
    String getID();

    Level getLevel();

    /**
     * tick for a world,process everything
     */
    void tick();

    //--- entity ---
    Collection<Entity> getAllEntities();

    Entity getEntity(String uid);

    void spawnEntity(String id, double x, double y, double z);

    void addEntity(Entity e);

    void removeEntity(String uid);

    void removeEntity(Entity e);

    //--- block ---
    /**
     * get a block from coordinate using(long x,long y,long z)
     * @param x x position
     * @param y y position
     * @param z z position
     * @return block object
     */
    BlockState getBlock(long x, long y, long z);

    /**
     * get a block from coordinate using(long x,long y,long z)
     * @param x x position
     * @param y y position
     * @param z z position
     * @param id block id
     * @param facing block facing
     *
     */
    void setBlock(long x, long y, long z, String id, BlockFacing facing);

    /**
     * set a block from coordinate using(long x,long y,long z),but will not cost nearby block update.
     * @param x x position
     * @param y y position
     * @param z z position
     * @param id block id
     * @param facing block facing
     *
     */
    void setBlockNoUpdate(long x, long y, long z, String id, BlockFacing facing);

    void setUpdate(long x, long y, long z);

    //--- chunk ---
    Chunk getChunk(ChunkPos p);

    Chunk getChunk(long cx, long cy, long cz);

    void loadChunk(ChunkPos p, ChunkLoadTicket ticket);

    void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket);

    void loadChunkRange(long centerCX, long centerCY, long centerCZ, int range, int ticks);

    long getTime();

    long getSeed();

    WorldInfo getWorldInfo();

    HashMapSet<ChunkPos, Chunk> getChunkCache();

    void loadChunkAndNear(long x,long y,long z,ChunkLoadTicket ticket);

    void addListener(WorldListener iWorldRenderer);

    void removeListener(WorldListener iWorldRenderer);
}
