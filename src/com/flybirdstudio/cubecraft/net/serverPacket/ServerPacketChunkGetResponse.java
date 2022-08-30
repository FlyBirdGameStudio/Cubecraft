package com.flybirdstudio.cubecraft.net.serverPacket;

import com.flybirdstudio.cubecraft.world.chunk.Chunk;
import com.flybirdstudio.util.net.Packet;

public record ServerPacketChunkGetResponse(Chunk chunk)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_get_response";
    }
}
