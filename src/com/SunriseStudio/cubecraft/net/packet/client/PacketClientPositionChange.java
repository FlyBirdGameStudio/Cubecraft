package com.SunriseStudio.cubecraft.net.packet.client;

import com.SunriseStudio.cubecraft.net.packet.Packet;
import com.SunriseStudio.cubecraft.util.NettyBufferBuilder;
import com.SunriseStudio.cubecraft.world.entity.EntityLocation;
import io.netty.buffer.ByteBuf;
import org.joml.Vector3d;

public record PacketClientPositionChange(EntityLocation location) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:player_pos_update";
    }

    @Override
    public ByteBuf serialize() {
        return NettyBufferBuilder.fromNBT(location.getData(),128);
    }
}
