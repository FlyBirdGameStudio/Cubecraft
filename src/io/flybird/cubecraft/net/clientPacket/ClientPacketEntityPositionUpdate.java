package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.world.entity.EntityLocation;
import io.flybird.util.net.Packet;

public record ClientPacketEntityPositionUpdate(String uuid, EntityLocation newLoc,EntityLocation oldLoc)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client_position_change";
    }
}