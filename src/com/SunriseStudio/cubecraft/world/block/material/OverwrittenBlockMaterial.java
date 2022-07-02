package com.SunriseStudio.cubecraft.world.block.material;

import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;

public class OverwrittenBlockMaterial extends IBlockMaterial{

    private final IBlockMaterial behavior;

    /**
     * this is an override block. if you want to change any attribute from parent,just override it.
     * @param material parent
     */
    public OverwrittenBlockMaterial(IBlockMaterial material){
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
