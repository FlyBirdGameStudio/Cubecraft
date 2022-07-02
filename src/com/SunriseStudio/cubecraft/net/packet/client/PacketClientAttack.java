package com.SunriseStudio.cubecraft.net.packet.client;

import com.SunriseStudio.cubecraft.net.packet.Packet;
import com.SunriseStudio.cubecraft.util.NettyBufferBuilder;
import com.SunriseStudio.cubecraft.util.collections.ArrayUtil;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagCompound;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientAttack(Entity from, Entity target) implements Packet {

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
