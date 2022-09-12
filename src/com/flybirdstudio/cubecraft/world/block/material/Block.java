package com.flybirdstudio.cubecraft.world.block.material;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.BlockFacing;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.cubecraft.world.entity.item.Item;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.util.math.HitBox;
import com.flybirdstudio.util.math.Vector3;
import org.joml.Vector3d;

/**
 * defines all data for a block.
 * STRUCTURE FINISHED-2022-6-14
 */
public abstract class Block {
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
    public void onBlockUpdate(IWorld dimension, long x, long y, long z) {
        //do nth
    }

    /**
     * when a block random ticks,this will be invoke.
     *
     * @param dimension happened dimention
     * @param x         happened position
     * @param y         happened position
     * @param z         happened position
     */
    public void onBlockRandomTick(IWorld dimension, long x, long y, long z) {
        //do nth
    }

    //  ------ metadata ------
    //this part is about attributes :)
    public abstract int getHardNess();

    public abstract int getBrakeLevel();

    public abstract String[] getTags();

    public abstract boolean isSolid();

    public abstract int opacity();

    /**
     * well, there is no BlockEntity,but for easier to understand,if you mark this as an blockEntity,it will update every tick;
     *
     * @return is block entity
     */
    public abstract boolean isBlockEntity();

    public Item[] getDrop(IWorld world, long x, long y, long z, Entity from) {
        return new Item[0];
    }

//  ------ general getter ------

    public AABB[] getCollisionBox(long x, long y, long z) {
        AABB[] aabbs = new AABB[getCollisionBoxSizes().length];
        for (int i = 0; i < getCollisionBoxSizes().length; i++) {
            AABB aabb = getCollisionBoxSizes()[i];
            aabbs[i] = new AABB(x + aabb.x0, y + aabb.y0, z + aabb.z0, x + aabb.x1, y + aabb.y1, z + aabb.z1);
        }
        return aabbs;
    }

    public HitBox[] getSelectionBox(long x, long y, long z, BlockState bs) {
        HitBox[] hits = new HitBox[getSelectionBoxSizes().length];
        for (int i = 0; i < getSelectionBoxSizes().length; i++) {
            AABB aabb = getCollisionBoxSizes()[i];
            hits[i] = new HitBox(new AABB(x + aabb.x0, y + aabb.y0, z + aabb.z0, x + aabb.x1, y + aabb.y1, z + aabb.z1), bs, new Vector3d(x, y, z));
        }
        return hits;
    }

    public boolean shouldRender(IWorld world, long x, long y, long z) {
        return !world.getBlockState(x, y, z).getBlock().isSolid();
    }

    public int getTexture(int face) {
        return 1;
    }

    public void onInteract(Entity from, IWorld world, long x, long y, long z, byte f) {
        Vector3<Long> pos = BlockFacing.findNear(x, y, z, 1, f);
        if (world.isfree(world.getBlockState(pos.x(), pos.y(), pos.z()).getCollisionBox(pos.x(), pos.y(), pos.z()))) {
            world.setBlock(pos.x(), pos.y(), pos.z(), "cubecraft:grass_block", BlockFacing.Up);
        }
    }

    public void onHit(Entity from, IWorld world, long x, long y, long z, byte f) {
        world.setBlock(x,y,z,"cubecraft:air",BlockFacing.Up);
    }
}
