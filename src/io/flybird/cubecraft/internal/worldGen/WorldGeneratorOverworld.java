package io.flybird.cubecraft.internal.worldGen;

import io.flybird.cubecraft.internal.block.BlockIdConstant;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.GenerateStatus;
import io.flybird.cubecraft.world.worldGen.WorldGenListener;

public class WorldGeneratorOverworld {
    @WorldGenListener(stage = GenerateStatus.BASE)
    public void generateChunkBase(Chunk chunk){
        for (int x=0;x<Chunk.WIDTH;x++){
            for (int z=0;z<Chunk.WIDTH;z++){
                for (int y=0;y<Chunk.HEIGHT;y++){
                    chunk.setBlockState(x,y,z, Registry.getBlockMap().get(BlockIdConstant.AIR).defaultState());
                }
            }
        }
    }


    @WorldGenListener(stage = GenerateStatus.BIOME)
    public void generateChunkNoise(Chunk chunk){

    }




    @WorldGenListener(stage = GenerateStatus.TERRAIN)
    public void generateChunkTerrain(Chunk c){

    }
}
