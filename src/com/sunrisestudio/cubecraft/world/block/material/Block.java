package com.sunrisestudio.cubecraft.world.block.material;

import com.sunrisestudio.cubecraft.world.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.world.entity.item.Item;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.util.math.HitBox;
import com.sunrisestudio.util.math.Vector3;
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
    public void onBlockUpdate(IWorldAccess dimension, long x, long y, long z) {
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
    public void onBlockRandomTick(IWorldAccess dimension, long x, long y, long z) {
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

    public Item[] getDrop(IWorldAccess world, long x, long y, long z, Entity from) {
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

    public HitBox[] getSelectionBox(IWorldAccess world, long x, long y, long z, BlockState bs) {
        HitBox[] hits = new HitBox[getSelectionBoxSizes().length];
        for (int i = 0; i < getSelectionBoxSizes().length; i++) {
            AABB aabb = getCollisionBoxSizes()[i];
            hits[i] = new HitBox(new AABB(x + aabb.x0, y + aabb.y0, z + aabb.z0, x + aabb.x1, y + aabb.y1, z + aabb.z1), bs, new Vector3d(x, y, z));
        }
        return hits;
    }

    public void render(IWorldAccess world, long x, long y, long z, long renderX, long renderY, long renderZ, BlockFacing facing, IVertexArrayBuilder builder) {
        //Registry.getBlockModelManager().get(this.id).render(builder,world,renderX,renderY,renderZ,x,y,z,facing);

        byte c1 = -1;
        byte c2 = -52;
        byte c3 = -103;

        if (this.shouldRender(world, x, y - 1, z)) {
            builder.colorB(c1, c1, c1);
            this.renderFace(builder, renderX, renderY, renderZ, 0);
        }
        if (this.shouldRender(world, x, y + 1, z)) {
            builder.colorB(c1, c1, c1);
            this.renderFace(builder, renderX, renderY, renderZ, 1);
        }
        if (this.shouldRender(world, x, y, z - 1)) {
            builder.colorB(c2, c2, c2);
            this.renderFace(builder, renderX, renderY, renderZ, 2);
        }
        if (this.shouldRender(world, x, y, z + 1)) {
            builder.colorB(c2, c2, c2);
            this.renderFace(builder, renderX, renderY, renderZ, 3);
        }
        if (this.shouldRender(world, x - 1, y, z)) {
            builder.colorB(c3, c3, c3);
            this.renderFace(builder, renderX, renderY, renderZ, 4);
        }
        if (this.shouldRender(world, x + 1, y, z)) {
            builder.colorB(c3, c3, c3);
            this.renderFace(builder, renderX, renderY, renderZ, 5);
        }
    }

    public boolean shouldRender(IWorldAccess world, long x, long y, long z) {
        return !world.getBlock(x, y, z).getBlock().isSolid();
    }

    public int getTexture(int face) {
        return 1;
    }

    public void renderFace(IVertexArrayBuilder builder, long x, long y, long z, int face) {
        int tex = this.getTexture(face);
        int xt = tex % 16 * 16;
        int yt = tex / 16 * 16;
        float u0 = (float) xt / 256.0f;
        float u1 = ((float) xt + 15.99f) / 256.0f;
        float v0 = (float) yt / 256.0f;
        float v1 = ((float) yt + 15.99f) / 256.0f;
        double x0 = (x + 0);
        double x1 = (x + 1);
        double y0 = (y + 0);
        double y1 = (y + 1);
        double z0 = (z + 0);
        double z1 = (z + 1);
        if (face == 0) {
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x0, y0, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u1, v0);
            builder.vertexUV(x1, y0, z1, u1, v1);
            return;
        }
        if (face == 1) {
            builder.vertexUV(x1, y1, z1, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y1, z1, u0, v1);
            return;
        }
        if (face == 2) {
            builder.vertexUV(x0, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z0, u0, v0);
            builder.vertexUV(x1, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z0, u1, v1);
            return;
        }
        if (face == 3) {
            builder.vertexUV(x0, y1, z1, u0, v0);
            builder.vertexUV(x0, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z1, u1, v1);
            builder.vertexUV(x1, y1, z1, u1, v0);
            return;
        }
        if (face == 4) {
            builder.vertexUV(x0, y1, z1, u1, v0);
            builder.vertexUV(x0, y1, z0, u0, v0);
            builder.vertexUV(x0, y0, z0, u0, v1);
            builder.vertexUV(x0, y0, z1, u1, v1);
            return;
        }
        if (face == 5) {
            builder.vertexUV(x1, y0, z1, u0, v1);
            builder.vertexUV(x1, y0, z0, u1, v1);
            builder.vertexUV(x1, y1, z0, u1, v0);
            builder.vertexUV(x1, y1, z1, u0, v0);
        }
    }

    public void onInteract(Entity from, IWorldAccess world, long x, long y, long z, byte f) {
        Vector3<Long> pos = BlockFacing.findNear(x, y, z, 1, f);
        if (world.isfree(world.getBlock(pos.x(), pos.y(), pos.z()).getCollisionBox(pos.x(), pos.y(), pos.z()))) {
            world.setBlock(pos.x(), pos.y(), pos.z(), "cubecraft:stone", BlockFacing.Up);
        }
    }

    public void onHit(Entity from, IWorldAccess world, long x, long y, long z, byte f) {
        world.setBlock(x,y,z,"cubecraft:air",BlockFacing.Up);
    }
}
