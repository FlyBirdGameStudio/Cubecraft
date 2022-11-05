package io.flybird.cubecraft.net.serverPacket;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.util.net.Packet;

public record ServerPacketChunkGetResponse(Chunk chunk)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_get_response";
    }
}
