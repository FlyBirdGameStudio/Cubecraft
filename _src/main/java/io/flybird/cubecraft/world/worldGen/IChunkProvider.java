package io.flybird.cubecraft.world.worldGen;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;

public interface IChunkProvider {
    Chunk loadChunk(ChunkPos pos);
}
