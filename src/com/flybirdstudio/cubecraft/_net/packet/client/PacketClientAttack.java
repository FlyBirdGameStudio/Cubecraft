package com.flybirdstudio.cubecraft.client.net.packet.client;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientAttack(Entity from, Entity target) implements ClientPacket {

    @Override
    public String getType() {
        return "cubecraft:client/attack";
    }

    @Override
    public ByteBuf serialize() {
        NBTTagCompound compound=new NBTTagCompound();
        compound.setCompoundTag("from",from.getData());
        compound.setCompoundTag("to",from.getData());
        return BufferBuilder.fromNBT(compound,1024);
    }
}
