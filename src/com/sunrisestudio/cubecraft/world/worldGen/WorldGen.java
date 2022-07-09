package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.cubecraft.world.chunk._Chunk;
import com.sunrisestudio.cubecraft.world._Level;

public class WorldGen {
    public _Level world;
    public long seed;
    public ChunkProv chunkProv;

    public WorldGen(_Level w, long seed){
        this.world=w;
        this.seed=seed;
        this.chunkProv =new ChunkProv(this.world,this.seed);
    }

    public _Chunk generateChunk(long cx, long cz) {
        return this.chunkProv.getChunk(cx, cz);
    }
}
