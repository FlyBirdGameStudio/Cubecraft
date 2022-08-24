package com.sunrisestudio.cubecraft.world.worldGen.pipeline.object;

import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.cubecraft.world.block.BlockState;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.CombinedNoise;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.sunrisestudio.cubecraft.world.worldGen.pipeline.IChunkGenerator;

import java.util.Random;

public class ChunkGeneratorOverWorld extends IChunkGenerator {
    private PerlinNoise altitude01;
    private PerlinNoise altitude02;
    private PerlinNoise erosion;
    private CombinedNoise altitude;

    @Override
    protected void init() {
        this.altitude01=new PerlinNoise(new Random(setting.seed()),8);
        this.altitude02=new PerlinNoise(new Random(setting.seed()&42378651025L-4678908571L|324684367),12);
        this.erosion=new PerlinNoise(new Random(setting.seed()|1263790017430924389L),8);
        this.altitude=new CombinedNoise(altitude01,altitude02);
    }

    @Override
    public void generate(Chunk chunk) {
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                for (int y=0;y<16;y++){
                    ChunkPos p=chunk.getKey();
                    double h=altitude.getValue(
                            p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.terrain.main.scale",64d),
                            p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.terrain.main.scale",64d)
                    )*32;
                    if(chunk.getKey().toWorldPosY(y)<=h) {
                        BlockState bs = chunk.getBlock(x, y, z);
                        bs.setId(Registry.getBiomeMap().get(bs.getBiome()).getBasicBlock());
                    }
                }
            }
        }
    }
}
