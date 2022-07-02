package com.SunriseStudio.cubecraft.net.netHandler;

import com.SunriseStudio.cubecraft.net.packet.Packet;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientInteractEntity(Entity from, Entity to) implements Packet {
    @Override
    public String getType() {
        return "cubecraft:client/interact_entity";
    }

    @Override
    public ByteBuf serialize() {
        return null;
    }
}
