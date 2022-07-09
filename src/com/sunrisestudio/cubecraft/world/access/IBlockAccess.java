package com.sunrisestudio.cubecraft.world.access;

import com.sunrisestudio.cubecraft.world.block.Block;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;

public interface IBlockAccess {

    /**
     * get a block from coordinate using(long x,long y,long z)
     * @param x x position
     * @param y y position
     * @param z z position
     * @return block object
     */
    Block getBlock(long x, long y, long z);

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
}
