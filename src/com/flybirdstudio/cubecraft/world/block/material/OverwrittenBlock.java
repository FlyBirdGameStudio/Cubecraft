package com.flybirdstudio.cubecraft.world.block.material;

import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.cubecraft.world.block.BlockFacing;

public class OverwrittenBlock extends Block {

    private final Block behavior;

    /**
     * this is an override block. if you want to change any attribute from parent,just override it.
     * @param material parent
     */
    public OverwrittenBlock(Block material){
        this.behavior=material;
    }

    @Override
    public BlockFacing[] getEnabledFacings() {
        return behavior.getEnabledFacings();
    }

    @Override
    public AABB[] getCollisionBoxSizes() {
        return behavior.getCollisionBoxSizes();
    }

    @Override
    public AABB[] getSelectionBoxSizes() {
        return behavior.getSelectionBoxSizes();
    }

    @Override
    public float getResistance() {
        return behavior.getResistance();
    }

    @Override
    public float getDensity() {
        return behavior.getDensity();
    }

    @Override
    public int getHardNess() {
        return behavior.getHardNess();
    }

    @Override
    public int getBrakeLevel() {
        return behavior.getBrakeLevel();
    }

    @Override
    public String[] getTags() {
        return behavior.getTags();
    }

    @Override
    public boolean isSolid() {
        return behavior.isSolid();
    }

    @Override
    public int opacity() {
        return behavior.opacity();
    }

    @Override
    public boolean isBlockEntity() {
        return behavior.isBlockEntity();
    }
}
