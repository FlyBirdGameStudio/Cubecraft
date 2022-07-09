package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientInteractEntity(Entity from, Entity to) implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:client/interact_entity";
    }

    @Override
    public ByteBuf serialize() {
        return null;
    }
}
