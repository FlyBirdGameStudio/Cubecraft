package com.flybirdstudio.cubecraft.client.net.packet.client;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientMessage(Entity sender, String message) implements ClientPacket {
    @Override
    public String getType() {
        return "cubecraft:client/message_send";
    }

    @Override
    public ByteBuf serialize() {
        NBTTagCompound compound=new NBTTagCompound();
        compound.setString("sender",sender.getUID());
        compound.setString("msg",message);

        return BufferBuilder.fromNBT(compound,message.length()+16);
    }
}
