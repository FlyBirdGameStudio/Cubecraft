package io.flybird.cubecraft.world.worldGen;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.container.options.Option;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;

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
