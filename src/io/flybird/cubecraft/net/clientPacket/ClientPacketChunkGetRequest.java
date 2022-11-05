package io.flybird.cubecraft.net.clientPacket;

import io.flybird.util.net.Packet;

public record ClientPacketChunkGetRequest (String uuid,long cx, long cy, long cz) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_get_request";
    }
}
