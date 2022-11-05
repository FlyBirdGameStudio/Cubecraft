package io.flybird.cubecraft.internal.blockbehavior;

import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.util.math.AABB;

public abstract class DefaultBlock extends Block {
    @Override
    public EnumFacing[] getEnabledFacings() {
        return EnumFacing.all();
    }

    @Override
    public AABB[] getCollisionBoxSizes() {
        return new AABB[]{new AABB(0,0,0,1,1,1)};
    }

    @Override
    public AABB[] getSelectionBoxSizes() {
        return new AABB[]{new AABB(0,0,0,1,1,1)};
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public int opacity() {
        return 0;
    }

}
