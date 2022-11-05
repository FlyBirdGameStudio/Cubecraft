package io.flybird.cubecraft.world.worldGen.pipeline;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.worldGen.WorldGeneratorSetting;

public abstract class IChunkGenerator {
    protected WorldGeneratorSetting setting;

    public void init(WorldGeneratorSetting setting){
        this.setting=setting;
        this.init();
    }

    protected abstract void init();

    public abstract void generate(Chunk chunk);
}
