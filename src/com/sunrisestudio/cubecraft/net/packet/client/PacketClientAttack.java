package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.util.nbt.NBTTagCompound;
import com.sunrisestudio.cubecraft.world.entity.Entity;
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
        return NettyBufferBuilder.fromNBT(compound,1024);
    }
}
