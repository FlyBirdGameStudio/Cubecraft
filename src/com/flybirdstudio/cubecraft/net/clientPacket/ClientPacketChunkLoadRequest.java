package com.flybirdstudio.cubecraft.net.clientPacket;

import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.util.net.Packet;

public record ClientPacketChunkLoadRequest(String uuid,long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_load_request";
    }
}
