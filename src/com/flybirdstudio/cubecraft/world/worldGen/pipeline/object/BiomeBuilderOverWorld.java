package com.flybirdstudio.cubecraft.world.worldGen.pipeline.object;

import com.flybirdstudio.cubecraft.registery.Registry;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.flybirdstudio.cubecraft.world.worldGen.pipeline.IChunkGenerator;

import java.util.Random;

public class BiomeBuilderOverWorld extends IChunkGenerator {
    private PerlinNoise altitude;
    private PerlinNoise temperature;
    private PerlinNoise erosion;
    private PerlinNoise humidity;
    private PerlinNoise continental;

    @Override
    public void init(){
        this.altitude=new PerlinNoise(new Random(setting.seed()),3);
        this.temperature=new PerlinNoise(new Random(setting.seed()&7312804879082317L),3);
        this.erosion=new PerlinNoise(new Random(setting.seed()|1263790017430924389L),3);
        this.humidity=new PerlinNoise(new Random(setting.seed()%16^13728402&3272184),3);
        this.continental=new PerlinNoise(new Random(setting.seed()^12368970&623179084743189L%13),3);
    }

    @Override
    public void generate(Chunk chunk) {
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                for (int y=0;y<16;y++){
                    ChunkPos p=chunk.getKey();
                    chunk.getBlockState(x,y,z).setBiome(Registry.getBiomeMap().match(
                            continental.getValue(
                                    p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",128d),
                                    p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",128d)
                            ),
                            temperature.getValue(
                                    p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.temperature.scale",128d),
                                    p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.temperature.scale",128d)
                            ),
                            humidity.getValue(
                                    p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.humidity.scale",128d),
                                    p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.humidity.scale",128d)
                            ),
                            erosion.getValue(
                                    p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.erosion.scale",128d),
                                    p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.erosion.scale",128d)
                            ),
                            altitude.getValue(
                                    p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.altitude.scale",128d),
                                    p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.altitude.scale",128d)
                            )
                    ).getId());
                }
            }
        }
    }
}
