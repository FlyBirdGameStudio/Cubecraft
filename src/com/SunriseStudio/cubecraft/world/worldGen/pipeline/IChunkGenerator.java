package com.sunrisestudio.cubecraft.world.worldGen.pipeline;

import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.worldGen.WorldGeneratorSetting;

public interface IChunkGenerator {
    void generate(Chunk chunk, WorldGeneratorSetting setting);
}
