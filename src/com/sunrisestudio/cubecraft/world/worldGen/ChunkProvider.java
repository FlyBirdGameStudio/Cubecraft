package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.IWorldAccess;

import java.util.concurrent.Callable;

public abstract class ChunkProvider implements Callable<Chunk> {
    private ChunkPos pos;
    private long seed;
    private Option worldProviderSetting;
    public IWorldAccess dimension;



    @Override
    public Chunk call() {
        return get(new Chunk(this.dimension, this.pos),this.seed,this.worldProviderSetting);
    }

    public abstract Chunk get(Chunk primer, long seed, Option providerSetting);
}
