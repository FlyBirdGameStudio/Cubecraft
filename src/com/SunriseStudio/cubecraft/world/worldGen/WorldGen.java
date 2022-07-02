package com.SunriseStudio.cubecraft.world.worldGen;

import com.SunriseStudio.cubecraft.world.chunk._Chunk;
import com.SunriseStudio.cubecraft.world.Level;

public class WorldGen {
    public Level world;
    public long seed;
    public ChunkProv chunkProv;

    public WorldGen(Level w, long seed){
        this.world=w;
        this.seed=seed;
        this.chunkProv =new ChunkProv(this.world,this.seed);
    }

    public _Chunk generateChunk(long cx, long cz) {
        return this.chunkProv.getChunk(cx, cz);
    }
}
