package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.util.container.options.Options;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.ImprovedNoise;

public class ChunkProviderOverworld extends ChunkProvider{
    public ChunkProviderOverworld(ChunkPos pos, long seed, World target) {

    }

    @Override
    public Chunk get(Chunk primer, long seed, Option providerSetting) {
        ImprovedNoise seaLand=new ImprovedNoise();
        double cx=primer.getKey().x()*16;
        double cy=primer.getKey().y()*16;
        double cz=primer.getKey().z()*16;


        /*
          step-1:generate sea and fill water;
          this step generates the basic sea filling.
          use a noise to decide whatever here is sea or land.
          if it is sea in map and coord is below sea level than block will fill by water.
         */
        double[][] land=new double[16][16];
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                float scaleH= (float) providerSetting.get("worldGen.overWorld.land.scaleH");
                float scaleV= (float) providerSetting.get("worldGen.overWorld.land.scaleV");
                float moveV= (float) providerSetting.get("worldGen.overWorld.land.moveV");
                land[x][z]=seaLand.getValue((cx+x)/scaleH,(cz+z)/scaleH)*scaleV+moveV;
                if (land[x][z]<0){
                    for (int y=0;y<16;y++){
                        if(cy+y<(long)providerSetting.get("worldGen.overWorld.seaLevel")) {
                            primer.setBlock(x, y, z, "water", BlockFacing.Up);
                        }
                    }
                }
            }
        }

        /*
          step-2:generate basic land(stone)
         */

        return primer;
    }


    static {
        Options.setDefault("worldGen.overWorld.land.scaleH",256.0f);
        Options.setDefault("worldGen.overWorld.land.scaleV",2048.0f);
        Options.setDefault("worldGen.overWorld.land.moveV",64f);
        Options.setDefault("worldGen.overWorld.seaLevel",0L);
    }
}
