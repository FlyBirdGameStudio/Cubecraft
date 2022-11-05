package io.flybird.cubecraft.net.clientPacket;

import io.flybird.util.net.Packet;

public record ClientPacketAttack(String uuid0, String uuid1)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client_packet_attack";
    }
}
