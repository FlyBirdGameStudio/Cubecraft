package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.util.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;

public record PacketClientEmpty() implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:empty";
    }

    @Override
    public ByteBuf serialize() {
        NBTTagCompound packet=new NBTTagCompound();
        packet.setString("type",getType());
        return NettyBufferBuilder.fromNBT(packet,50);
    }
}
