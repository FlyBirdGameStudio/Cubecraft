package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.BufferBuilder;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
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
        return BufferBuilder.fromNBT(packet,50);
    }
}
