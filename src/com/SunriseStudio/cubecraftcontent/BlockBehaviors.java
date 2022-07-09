package com.sunrisestudio.cubecraftcontent;

import com.sunrisestudio.util.math.AABB;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.block.material.IBlockMaterial;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.BlockBehaviorGetter;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.BlockBehaviorRegistery;
import com.sunrisestudio.cubecraft.world.block.registery.behavior.IBlockBehaviorRegistery;

@BlockBehaviorRegistery(namespace = "minecraft")
public class BlockBehaviors implements IBlockBehaviorRegistery {
    @BlockBehaviorGetter(id = "stone")
    public IBlockMaterial stone(){
        return new IBlockMaterial() {
            @Override
            public BlockFacing[] getEnabledFacings() {
                return BlockFacing.all();
            }

            @Override
            public AABB[] getCollisionBoxSizes() {
                return new AABB[]{new AABB(0, 0, 0, 1, 1, 1)};
            }

            @Override
            public AABB[] getSelectionBoxSizes() {
                return new AABB[]{new AABB(0, 0, 0, 1, 1, 1)};
            }

            @Override
            public float getResistance() {
                return 0;
            }

            @Override
            public float getDensity() {
                return 10;
            }

            @Override
            public int getHardNess() {
                return 4;
            }

            @Override
            public int getBrakeLevel() {
                return 2;
            }

            @Override
            public String[] getTags() {
                return new String[0];
            }

            @Override
            public boolean isSolid() {
                return true;
            }

            @Override
            public int opacity() {
                return 0;//value is solid ,ignored it
            }

            @Override
            public boolean isBlockEntity() {
                return false;
            }
        };
    }

    @BlockBehaviorGetter(id = "untouchableBlock")
    public IBlockMaterial untouchableBlock(){
        return new IBlockMaterial() {
            @Override
            public BlockFacing[] getEnabledFacings() {
                return BlockFacing.all();
            }

            @Override
            public AABB[] getCollisionBoxSizes() {
                return new AABB[0];
            }

            @Override
            public AABB[] getSelectionBoxSizes() {
                return new AABB[0];
            }

            @Override
            public float getResistance() {
                return 0;
            }

            @Override
            public float getDensity() {
                return 10;
            }

            @Override
            public int getHardNess() {
                return 0;
            }

            @Override
            public int getBrakeLevel() {
                return 0;
            }

            @Override
            public String[] getTags() {
                return new String[0];
            }

            @Override
            public boolean isSolid() {
                return false;
            }

            @Override
            public int opacity() {
                return 0;
            }

            @Override
            public boolean isBlockEntity() {
                return false;
            }
        };
    }
}
