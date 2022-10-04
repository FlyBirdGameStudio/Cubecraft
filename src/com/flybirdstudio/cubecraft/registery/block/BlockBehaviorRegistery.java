package com.flybirdstudio.cubecraft.registery.block;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.block.material.Block;
import com.flybirdstudio.util.math.AABB;
import com.flybirdstudio.cubecraft.world.block.EnumFacing;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;

public class BlockBehaviorRegistery {
    @NameSpaceItemGetter(id = "block",namespace="cubecraft")
    public Block block(){
        return new Block() {
            @Override
            public EnumFacing[] getEnabledFacings() {
                return EnumFacing.all();
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
                return 1;
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


    @NameSpaceItemGetter(id = "stone",namespace="cubecraft")
    public Block stone(){
        return new Block() {

            @Override
            public EnumFacing[] getEnabledFacings() {
                return EnumFacing.all();
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
                return 1;
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

    @NameSpaceItemGetter(id = "block_leaves",namespace="cubecraft")
    public Block leaves(){
        return new Block() {

            @Override
            public EnumFacing[] getEnabledFacings() {
                return EnumFacing.all();
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
                return 1;
            }

            @Override
            public float getDensity() {
                return 2;
            }

            @Override
            public int getHardNess() {
                return 2;
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
                return 0;//value is solid ,ignored it
            }

            @Override
            public boolean isBlockEntity() {
                return false;
            }
        };
    }

    @NameSpaceItemGetter(id = "air",namespace="cubecraft")
    public Block untouchableBlock(){
        return new Block() {
            @Override
            public EnumFacing[] getEnabledFacings() {
                return EnumFacing.all();
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

            @Override
            public boolean shouldRender(IWorld world, long x, long y, long z) {
                return false;
            }
        };
    }

    @NameSpaceItemGetter(id = "dirt",namespace="cubecraft")
    public Block dirt(){
        return new Block() {
            @Override
            public EnumFacing[] getEnabledFacings() {
                return new EnumFacing[]{EnumFacing.Up};
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
                return 1;
            }

            @Override
            public float getDensity() {
                return 0.48f;
            }

            @Override
            public int getHardNess() {
                return 4;
            }

            @Override
            public int getBrakeLevel() {
                return 0;
            }

            @Override
            public String[] getTags() {
                return new String[]{
                        "cubecraft:farm_convertible",
                        "cubecraft:path_convertible",
                        "cubecraft:plantable"
                };
            }

            @Override
            public boolean isSolid() {
                return true;
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