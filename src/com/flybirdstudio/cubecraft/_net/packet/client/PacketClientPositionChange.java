package com.flybirdstudio.cubecraft.client.net.packet.client;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.cubecraft.world.entity.EntityLocation;
import io.netty.buffer.ByteBuf;

public record PacketClientPositionChange(EntityLocation location) implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:player_pos_update";
    }

    @Override
    public ByteBuf serialize() {
        return BufferBuilder.fromNBT(location.getData(),128);
    }
}
