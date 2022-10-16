package com.flybirdstudio.cubecraft.world.worldGen.pipeline.object;

import com.flybirdstudio.cubecraft.Registry;
import com.flybirdstudio.cubecraft.world.block.BlockState;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.flybirdstudio.cubecraft.world.worldGen.noiseGenerator.Synth;
import com.flybirdstudio.cubecraft.world.worldGen.pipeline.IChunkGenerator;
import com.flybirdstudio.cubecraft.world.worldGen.templete.Modification;
import com.flybirdstudio.cubecraft.world.worldGen.templete.Scale;
import com.flybirdstudio.cubecraft.world.worldGen.templete.Select;
import com.flybirdstudio.util.math.MathHelper;

import java.util.Random;

public class ChunkGeneratorOverWorld extends IChunkGenerator {

    private Synth altitudeHigh;
    private Synth altitudeLow;
    private Synth altitudeFinal;

    private Synth flatness;

    private Synth erosion;

    private Synth continental;
    private Synth peaks;

    @Override
    protected void init() {
        this.erosion=new Modification(1/64f,0,4,0,new PerlinNoise(new Random(setting.seed()|1263790017430924389L),3));
        this.continental=new Scale(new PerlinNoise(new Random(setting.seed()^12368970&623179084743189L%13),5),512,512);

        this.altitudeHigh =new Modification(1/32f,0,2.4,6,new PerlinNoise(new Random(setting.seed()),3));
        this.altitudeLow =new Modification(1/32f,0,2.4,-3,new PerlinNoise(new Random(setting.seed()&42378651025L-4678908571L|324684367),4));
        this.altitudeFinal =new Select(altitudeHigh, altitudeLow,erosion);

        this.peaks=new Modification(1/16f,0,8,-72,new PerlinNoise(new Random(setting.seed()^41637890126505473L|31897072),12));
        this.flatness=new Modification(1/64f,0,2,0,new PerlinNoise(new Random(setting.seed()^1790473&2378190472L+132899),3));
    }

    @Override
    public void generate(Chunk chunk) {
        double[][] heightMap=new double[16][16];
        ChunkPos p=chunk.getKey();
        for (int x=0;x<16;x++){
            for (int z=0;z<16;z++){
                    heightMap[x][z]=
                            Math.max(altitudeFinal.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)),-100000
                            //flatness.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)>1.0f? peaks.getValue(p.toWorldPosX(x),p.toWorldPosZ(z)) : -1145141919810L)
                            )+continental.getValue(
                        p.toWorldPosX(x)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",512d),
                        p.toWorldPosZ(z)/setting.getValueOrDefaultAsDouble("overworld.biome.continental.scale",512d)
                );
                    int h= (int) MathHelper.getRelativePosInChunk((long) heightMap[x][z],16);
                for (int y=0;y<16;y++){
                    if(chunk.getKey().toWorldPosY(y)<=heightMap[x][z]) {
                        BlockState bs = chunk.getBlockState(x, y, z);
                        bs.setId(Registry.getBiomeMap().get(bs.getBiome()).getBasicBlock());
                    }
                }
                Registry.getBiomeMap().get(chunk.getBlockState(x,h,z).getBiome()).buildSurface(chunk,x,z,heightMap[x][z],setting.seed());
            }
        }
    }
}
