package com.SunriseStudio.cubecraft.net.netHandler;

import com.SunriseStudio.cubecraft.net.packet.Packet;
import com.SunriseStudio.cubecraft.util.NettyBufferBuilder;
import com.SunriseStudio.cubecraft.util.nbt.NBTTagCompound;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import io.netty.buffer.ByteBuf;

public record PacketClientMessage(Entity sender, String message) implements Packet {
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
