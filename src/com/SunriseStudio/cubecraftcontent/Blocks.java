package com.sunrisestudio.cubecraftcontent;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.block.material.IBlockMaterial;
import com.sunrisestudio.cubecraft.world.block.material.OverwrittenBlockMaterial;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;
import com.sunrisestudio.cubecraft.world.block.registery.block.BlockGetter;
import com.sunrisestudio.cubecraft.world.block.registery.block.BlockRegistery;
import com.sunrisestudio.cubecraft.world.block.registery.block.IBlockRegistery;

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
            public void onBlockRandomTick(IWorldAccess dimension, long x, long y, long z) {
                if(!dimension.getBlockAccess().getBlock(x,y+1,z).getMaterial().isSolid()){
                    dimension.getBlockAccess().setBlockNoUpdate(x,y,z,"grass_block", BlockFacing.Up);
                }
            }
        };
    }


}
