package io.flybird.cubecraft.internal.world.worldGen;

import io.flybird.cubecraft.internal.WorldType;
import io.flybird.cubecraft.register.ContentRegistry;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.cubecraft.world.worldGen.pipeline.GenerateStage;
import io.flybird.cubecraft.world.worldGen.WorldGenListener;
import io.flybird.cubecraft.world.worldGen.WorldGeneratorSetting;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import io.flybird.cubecraft.world.worldGen.noiseGenerator.Synth;
import io.flybird.cubecraft.world.worldGen.pipeline.IChunkGenerator;
import io.flybird.cubecraft.world.worldGen.templete.Scale;

import java.util.Random;

public class WorldGeneratorOverworld implements IChunkGenerator {
    private PerlinNoise altitude;
    private PerlinNoise temperature;
    private PerlinNoise erosion;
    private PerlinNoise humidity;
    private PerlinNoise continental;
    private Synth altitudeFinal;

    @WorldGenListener(stage = GenerateStage.BASE,world = WorldType.OVERWORLD)
    public void generateChunkBase(Chunk chunk, WorldGeneratorSetting setting){
        /*
        for (int x=0;x<Chunk.WIDTH;x++){
            for (int z=0;z<Chunk.WIDTH;z++){
                for (int y=0;y<Chunk.HEIGHT;y++){
                    chunk.setBlockState(x,y,z, Registry.getBlockMap().get(BlockType.AIR).defaultState());
                }
            }
        }
         */
        /*
        this.altitude=new PerlinNoise(new Random(setting.seed()),3);
        this.temperature=new PerlinNoise(new Random(setting.seed()&7312804879082317L),3);
        this.erosion=new PerlinNoise(new Random(setting.seed()|1263790017430924389L),3);
        this.humidity=new PerlinNoise(new Random(setting.seed()%16^13728402&3272184),3);
        this.continental=new PerlinNoise(new Random(setting.seed()^12368970&623179084743189L%13),3);
         */
        //Synth altitudeHigh = new Modification(1 / 32f, 0, 2.4, 6, new PerlinNoise(new Random(setting.seed()), 3));
        //Synth altitudeLow = new Modification(1 / 32f, 0, 2.4, -3, new PerlinNoise(new Random(setting.seed() & 42378651025L - 4678908571L | 324684367), 4));
        this.altitudeFinal =new Scale(new PerlinNoise(new Random(setting.seed()),3),32,32);
               // new Select(altitudeHigh, altitudeLow,new Scale(new PerlinNoise(new Random(setting.seed()%738210^1327899),4),32d,32d));
    }


    //@WorldGenListener(stage = GenerateStage.BIOME,world = WorldType.OVERWORLD)
    public void generateChunkBiome(Chunk chunk,WorldGeneratorSetting setting){
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                for (int y=0;y<Chunk.HEIGHT;y++){
                    ChunkPos p=chunk.getKey();
                    chunk.setBiome(x,y,z, ContentRegistry.getBiomeMap().match(
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

    @WorldGenListener(stage = GenerateStage.TERRAIN,world = WorldType.OVERWORLD)
    public void generateChunkTerrain(Chunk chunk, WorldGeneratorSetting setting){
        double[][] heightMap=new double[16][16];

        ChunkPos p=chunk.getKey();

        /*
        double _00=altitudeFinal.getValue(p.toWorldPosX(0),p.toWorldPosZ(0));
        double _01=altitudeFinal.getValue(p.toWorldPosX(0),p.toWorldPosZ(16));
        double _10=altitudeFinal.getValue(p.toWorldPosX(16),p.toWorldPosZ(0));
        double _11=altitudeFinal.getValue(p.toWorldPosX(16),p.toWorldPosZ(16));
        */

        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                /*
                heightMap[x][z]= MathHelper.linear_interpolate2d(_00,_01,_10,_11,x/16f,z/16f);

                heightMap[x][z]=
                        Math.max(altitudeFinal.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)),-100000
                                //flatness.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)>1.0f? peaks.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)) : -1145141919810L)
                        )+continental.getValue(
                                p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",512d),
                                p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",512d)
                        );

                 */
                for (int y=0;y<Chunk.HEIGHT;y++){
                    if(chunk.getKey().toWorldPosY(y)<=-2) {
                        BlockState bs = chunk.getBlockState(x, y, z);
                        chunk.setBlockState(x,y,z,bs.setId("cubecraft:stone"));
                    }
                }
            }
        }
    }
}
