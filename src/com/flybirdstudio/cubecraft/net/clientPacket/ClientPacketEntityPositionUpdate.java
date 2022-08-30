package com.flybirdstudio.cubecraft.net.clientPacket;

import com.flybirdstudio.cubecraft.world.entity.EntityLocation;
import com.flybirdstudio.util.net.Packet;

public record ClientPacketEntityPositionUpdate(String uuid, EntityLocation newLoc,EntityLocation oldLoc)implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client_position_change";
    }
}