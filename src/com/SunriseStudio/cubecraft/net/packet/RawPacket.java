package com.sunrisestudio.cubecraft.net.packet;

import com.sunrisestudio.util.container.BufferBuilder;
import com.sunrisestudio.util.event.TypeGettableEventItem;
import com.sunrisestudio.util.file.nbt.tag.NBTTagCompound;
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
        return BufferBuilder.fromNBT(nbt, 50);
    }

    /**
     * get a wrapped buffer from {@link ByteBuf}
     * @param bytebuf data source
     * @return wrapped buffer
     */
    public static RawPacket unSerialize(ByteBuf bytebuf) {
        NBTTagCompound nbt = (NBTTagCompound) BufferBuilder.toNBT(bytebuf);
        return new RawPacket(
                nbt.getString("type"),
                BufferBuilder.wrap(nbt.getByteArray("data"))
        );
    }

    @Override
    public String getType() {
        return null;
    }
}
