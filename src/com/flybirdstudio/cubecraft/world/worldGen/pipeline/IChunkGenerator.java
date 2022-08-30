package com.flybirdstudio.cubecraft.world.worldGen.pipeline;

import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.cubecraft.world.worldGen.WorldGeneratorSetting;

public abstract class IChunkGenerator {
    protected WorldGeneratorSetting setting;

    public void init(WorldGeneratorSetting setting){
        this.setting=setting;
        this.init();
    }

    protected abstract void init();

    public abstract void generate(Chunk chunk);
}
