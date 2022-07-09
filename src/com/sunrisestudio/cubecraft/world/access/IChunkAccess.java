package com.sunrisestudio.cubecraft.world.access;

import com.sunrisestudio.cubecraft.world.chunk.Chunk;
import com.sunrisestudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.sunrisestudio.cubecraft.world.chunk.ChunkPos;

public interface IChunkAccess {
    //--- chunk ---
    Chunk getChunk(ChunkPos p);

    Chunk getChunk(long cx, long cy, long cz);

    void loadChunk(ChunkPos p, ChunkLoadTicket ticket);

    void loadChunk(long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket);

    void loadChunkRange(long centerCX, long centerCY, long centerCZ, byte range, int ticks);
}
