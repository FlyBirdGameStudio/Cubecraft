package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.util.net.Packet;

public record ClientPacketChunkLoadRequest(String uuid,long cx, long cy, long cz, ChunkLoadTicket chunkLoadTicket) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_load_request";
    }
}
