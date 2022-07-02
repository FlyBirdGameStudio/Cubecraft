package com.SunriseStudio.cubecraft.world.block.registery.block;
import com.SunriseStudio.cubecraft.world.block.BlockFacing;
import com.SunriseStudio.cubecraft.world.block.material.IBlockMaterial;
import com.SunriseStudio.cubecraft.world.block.material.OverwrittenBlockMaterial;
import com.SunriseStudio.cubecraft.world.block.registery.BlockMap;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;

@BlockRegistery(namespace = "minecraft")
public class Blocks implements IBlockRegistery {
    @BlockGetter(id = "air",behavior = "untouchableBlock")
    public IBlockMaterial air(IBlockMaterial behavior){
        return new OverwrittenBlockMaterial(behavior){

        };
    }

    @BlockGetter(id = "stone",behavior = "stone")
    public IBlockMaterial stone(IBlockMaterial behavior){
        return new OverwrittenBlockMaterial(behavior);
    }

    @BlockGetter(id="dirt",behavior = "dirt")
    public IBlockMaterial dirt(IBlockMaterial behavior){
        return new OverwrittenBlockMaterial(behavior){
            @Override
            public void onBlockRandomTick(IDimensionAccess dimension, long x, long y, long z) {
                if(!dimension.getBlock(x,y+1,z).getMaterial().isSolid()){
                    dimension.setBlockNoUpdate(x,y,z,"grass_block", BlockFacing.Up);
                }
            }
        };
    }


    static {
        BlockMap.addBlockRegistery(Blocks.class);
    }
}
