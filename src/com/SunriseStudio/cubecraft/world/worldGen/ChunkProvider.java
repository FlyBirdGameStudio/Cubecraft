package com.SunriseStudio.cubecraft.world.worldGen;

import com.SunriseStudio.cubecraft.util.collections.options.Option;
import com.SunriseStudio.cubecraft.world.chunk.Chunk;
import com.SunriseStudio.cubecraft.world.chunk.ChunkPos;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;

import java.util.concurrent.Callable;

public abstract class ChunkProvider implements Callable<Chunk> {
    private final ChunkPos pos;
    private final long seed;
    private final Option worldProviderSetting;
    private final IDimensionAccess dimension;

    public ChunkProvider(ChunkPos pos, long seed, IDimensionAccess target){
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
