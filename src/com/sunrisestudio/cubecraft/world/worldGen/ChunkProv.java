package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.ImprovedNoise;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.PerlinNoise;
import com.sunrisestudio.cubecraft.world.chunk._Chunk;
import com.sunrisestudio.cubecraft.world._Level;
import com.sunrisestudio.cubecraft.world.block.Tile;
import com.sunrisestudio.cubecraft.world.worldGen.noiseGenerator.Synth;

import java.util.Random;

public class ChunkProv {
    public long seed;
    public _Level world;
    public PerlinNoise noise;
    public Synth peak_n_valley;

    public long seaLevel=128;
    public ChunkProv(_Level world, long seed) {
        this.world=world;
        this.seed=seed;
        this.noise=new PerlinNoise(new Random(seed),2);

        //别管魔法数字，都是我用脸在键盘上滚出来的
        this.peak_n_valley=new ImprovedNoise(new Random(seed));
    }

    public _Chunk getChunk(long cx, long cz){
        double[][] heightMap=new double[16][16];
        _Chunk c=new _Chunk(cx,cz,world);
        for (int i = 0; i < _Chunk.W; i++) {
            for (int k = 0; k < _Chunk.W; k++) {
                heightMap[i][k] = seaLevel +
                        //noise.getValue((cx*16+i)/noise_scale,(cz*16+k)/noise_scale)*biomesMap[i][k].contrast+biomesMap[i][k].height*
                        peak_n_valley.getValue((cx*16+i)/256f,(cz*16+k)/256f)*32;
                for (int j = 1; j < seaLevel; j++) {
                        c.setBlock(i,j,k,(byte) Tile.water.id);
                    }//add water
                for (int j = 0; j < heightMap[i][k]; j++) {
                    c.setBlock(i,j,k,(byte) Tile.rock.id);
                }
                c.setBlock(i,0,k, (byte) Tile.bedrock.id);
            }
        }
        return c;
    }
}
