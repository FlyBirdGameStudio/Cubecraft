package com.SunriseStudio.cubecraft.world.block.material;

import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.util.math.HitBox;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import com.SunriseStudio.cubecraft.world.entity.item.Item;

/**
 * defines all data for a block.
 * STRUCTURE FINISHED-2022-6-14
 */
public abstract class IBlockMaterial {
    private String id;
//  ------ physic ------

    /**
     * defines which facings are valid
     *
     * @return facings
     */
    public abstract BlockFacing[] getEnabledFacings();

    /**
     * defines collision boxes(relative)
     *
     * @return boxes
     */
    public abstract AABB[] getCollisionBoxSizes();

    /**
     * defines selection boxes(relative)
     *
     * @return boxes
     */
    public abstract AABB[] getSelectionBoxSizes();

    /**
     * defines resistance of a block.if entity goes into it,speed will multiply this value.
     *
     * @return resistance(0.0 ~ 1.0)
     */
    public abstract float getResistance();

    /**
     * defines whatever entity flows up or gets down when into it base on the value bigger or smaller from entity`s density
     *
     * @return density
     */
    public abstract float getDensity();

//  ------ ticking ------

    /**
     * when a block updates,this will be invoke.
     *
     * @param dimension happened dimention
     * @param x         happened position
     * @param y         happened position
     * @param z         happened position
     */
    public void onBlockUpdate(IDimensionAccess dimension, long x, long y, long z) {
        //do nth
    }

    ;

    /**
     * when a block random ticks,this will be invoke.
     *
     * @param dimension happened dimention
     * @param x         happened position
     * @param y         happened position
     * @param z         happened position
     */
    public void onBlockRandomTick(IDimensionAccess dimension, long x, long y, long z) {
        //do nth
    }

    //  ------ metadata ------
    //this part is about attributes :)
    public abstract int getHardNess();

    public abstract int getBrakeLevel();

    public abstract String[] getTags();

    public abstract boolean isSolid();

    public abstract int opacity();

    public String getId() {
        return this.id;
    }

    /**
     * well, there is no BlockEntity,but for easier to understand,if you mark this as an blockEntity,it will update every tick;
     * @return is block entity
     */
    public abstract boolean isBlockEntity();

    public Item[] getDrop(IDimensionAccess world, long x, long y, long z, Entity from) {
        return new Item[0];
    }

//  ------ general getter ------

    public AABB[] getCollisionBox(long x, long y, long z) {
        AABB[] aabbs = new AABB[getCollisionBoxSizes().length];
        for (int i=0;i<getSelectionBoxSizes().length;i++){
            aabbs[i]=getCollisionBoxSizes()[i].cloneMove(x,y,z);
        }
        return aabbs;
    }

    public HitBox[] getSelectionBox(long x, long y, long z) {
        AABB[] aabbs = getSelectionBoxSizes();
        HitBox[] hits=new HitBox[getSelectionBoxSizes().length];
        for (int i=0;i<getSelectionBoxSizes().length;i++){
            hits[i]=new HitBox(aabbs[i].cloneMove(x, y, z), (iDimensionAccess, from) -> {
                //todo:add hit logic
            });
        }
        return hits;
    }


}
