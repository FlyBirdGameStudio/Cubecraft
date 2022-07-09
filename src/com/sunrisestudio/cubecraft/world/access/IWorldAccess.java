package com.sunrisestudio.cubecraft.world.access;

import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.cubecraft.world.Level;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.util.math.AABB;

import java.util.ArrayList;

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

    /**
     * get access object for block
     * @return block access object
     */
    IBlockAccess getBlockAccess();

    /**
     * get access object for chunk
     * @return block access object
     */
    IChunkAccess getChunkAccess();

    /**
     * get access object for entity
     * @return block access object
     */
    IEntityAccess getEntityAccess();
}
