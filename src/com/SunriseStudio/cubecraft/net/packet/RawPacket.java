package com.sunrisestudio.cubecraft.net.packet;

import com.sunrisestudio.util.container.buffer.NettyBufferBuilder;
import com.sunrisestudio.util.event.TypeGettableEventItem;
import com.sunrisestudio.util.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;

public record RawPacket(String type, ByteBuf data) implements TypeGettableEventItem {

    /**
     * pack a packet from {@link RawPacket}
     * @param packet packet
     * @return bytebuf
     */
    public static ByteBuf serialize(RawPacket packet) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", packet.getType());
        nbt.setByteArray("data", packet.data.array());
        return NettyBufferBuilder.fromNBT(nbt, 50);
    }

    /**
     * get a wrapped buffer from {@link ByteBuf}
     * @param bytebuf data source
     * @return wrapped buffer
     */
    public static RawPacket unSerialize(ByteBuf bytebuf) {
        NBTTagCompound nbt = (NBTTagCompound) NettyBufferBuilder.toNBT(bytebuf);
        return new RawPacket(
                nbt.getString("type"),
                NettyBufferBuilder.wrap(nbt.getByteArray("data"))
        );
    }

    @Override
    public String getType() {
        return null;
    }
}
