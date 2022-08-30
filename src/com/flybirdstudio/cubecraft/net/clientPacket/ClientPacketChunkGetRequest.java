package com.flybirdstudio.cubecraft.net.clientPacket;

import com.flybirdstudio.util.net.Packet;

public record ClientPacketChunkGetRequest (String uuid,long cx, long cy, long cz) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:chunk_get_request";
    }
}
