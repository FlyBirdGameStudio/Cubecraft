package com.flybirdstudio.cubecraft.registery;

import com.flybirdstudio.cubecraft.world.biome.Biome;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.flybirdstudio.util.container.namespace.NameSpaceItemGetter;
import com.flybirdstudio.util.math.MathHelper;

import java.util.Random;


public class BiomesRegistry {
    @NameSpaceItemGetter(id = "plains", namespace = "cubecraft")
    public Biome plains(){
        return new Biome(0.3, 0.5, 0.4, 0, 0.03,"cubecraft:plains", "cubecraft:stone", 0x91bd59,0x77ab2f){
            @Override
            public void buildSurface(Chunk primer,int x, int z, double height, long seed) {
                ChunkPos p=primer.getKey();
                PerlinNoise noise=new PerlinNoise(new Random(seed),3);
                int relativeHeight=(int) MathHelper.getRelativePosInChunk((long) height,16);
                int levels= (int) noise.getValue(p.toWorldPosX(x),p.toWorldPosZ(z))/2+5;
                for (int y=0;y<16;y++){
                    long h=p.toWorldPosY(y);
                    if(h==(long)height){
                        primer.getBlockState(x,relativeHeight,z).setId("cubecraft:grass_block");
                    }
                    if(h<(long)height&&h>(long)height-levels){
                        primer.getBlockState(x,y,z).setId("cubecraft:dirt");
                    }
                }
            }
        };
    }
}
