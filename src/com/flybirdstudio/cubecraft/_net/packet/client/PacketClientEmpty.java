package com.flybirdstudio.cubecraft.client.net.packet.client;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
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
