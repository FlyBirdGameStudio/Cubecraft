package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.cubecraft.world.entity.EntityLocation;
import io.netty.buffer.ByteBuf;

public record PacketClientPositionChange(EntityLocation location) implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:player_pos_update";
    }

    @Override
    public ByteBuf serialize() {
        return NettyBufferBuilder.fromNBT(location.getData(),128);
    }
}
