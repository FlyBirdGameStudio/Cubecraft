package com.sunrisestudio.cubecraft.net.packet.client;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.util.nbt.NBTTagCompound;
import com.sunrisestudio.cubecraft.world.entity.Entity;
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

        return NettyBufferBuilder.fromNBT(compound,message.length()+16);
    }
}
