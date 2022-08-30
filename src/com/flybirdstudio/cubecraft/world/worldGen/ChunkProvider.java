package com.flybirdstudio.cubecraft.world.worldGen;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.util.container.options.Option;
import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.chunk.ChunkPos;

import java.util.concurrent.Callable;

public abstract class ChunkProvider implements Callable<Chunk> {
    private ChunkPos pos;
    private long seed;
    private Option worldProviderSetting;
    public IWorld dimension;



    @Override
    public Chunk call() {
        return get(new Chunk(this.dimension, this.pos),this.seed,this.worldProviderSetting);
    }

    public abstract Chunk get(Chunk primer, long seed, Option providerSetting);
}
