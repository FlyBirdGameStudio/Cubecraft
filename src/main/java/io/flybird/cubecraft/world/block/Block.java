package io.flybird.cubecraft.world.block;

import io.flybird.cubecraft.client.render.worldObjectRenderer.BlockModelRenderer;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.entity.item.Item;
import io.flybird.util.math.AABB;
import io.flybird.util.math.HitBox;
import io.flybird.util.container.Vector3;
import org.joml.Vector3d;

/**
 * defines all data for a block.
 * STRUCTURE FINISHED-2022-6-14
 */
public abstract class Block {
    public Block(String defaultRegisterId) {
        Registries.BLOCK_RENDERER.registerItem(defaultRegisterId, new BlockModelRenderer(ResourceLocation.blockModel(defaultRegisterId.split(":")[0], defaultRegisterId.split(":")[1] + ".json")));
    }

    public Block() {
    }
//  ------ physic ------

    /**
     * defines which facings are valid
     *
     * @return facings
     */
    public abstract EnumFacing[] getEnabledFacings();

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

    public abstract int getHardNess();

    public abstract boolean isSolid();

    public abstract int opacity();

    public int getLight() {
        return 0;
    }

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
    public abstract String getID();

    public abstract String[] getTags();

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

    public void onInteract(Entity from, IWorld world, long x, long y, long z, byte f) {
        //todo:use inventory
        Vector3<Long> pos = EnumFacing.findNear(x, y, z, 1, f);
        if (world.isFree(Registries.BLOCK.get(from.getSelectBlock()).getCollisionBox(pos.x(), pos.y(), pos.z()))) {
            world.setBlockState(pos.x(), pos.y(), pos.z(), Registries.BLOCK.get(from.getSelectBlock()).defaultState(x,y,z));
        }
    }

    public void onHit(Entity from, IWorld world, long x, long y, long z, byte f) {
        world.setBlockState(x, y, z, Registries.BLOCK.get("cubecraft:air").defaultState(x, y, z));
    }

    public BlockState defaultState(long x, long y, long z) {
        return new BlockState(this.getID(), (byte) 0, (byte) 0).setX(x).setY(y).setZ(z);
    }

    public abstract int light();
}
