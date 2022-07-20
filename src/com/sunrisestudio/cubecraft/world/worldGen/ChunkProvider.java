package com.sunrisestudio.cubecraft.world.worldGen;

import com.sunrisestudio.util.container.options.Option;
import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;
import com.sunrisestudio.cubecraft.world.IWorldAccess;

import java.util.concurrent.Callable;

public abstract class ChunkProvider implements Callable<Chunk> {
    private final ChunkPos pos;
    private final long seed;
    private final Option worldProviderSetting;
    private final IWorldAccess dimension;

    public ChunkProvider(ChunkPos pos, long seed, IWorldAccess target){
        this.dimension=target;
        this.worldProviderSetting=new Option("worldGen.provider");
        this.seed=seed;
        this.pos=pos;
    }

    @Override
    public Chunk call() {
        return get(new Chunk(this.dimension, this.pos),this.seed,this.worldProviderSetting);
    }

    public abstract Chunk get(Chunk primer, long seed, Option providerSetting);
}
