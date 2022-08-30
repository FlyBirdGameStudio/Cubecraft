package com.flybirdstudio.cubecraft.client.net.packet;

import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;

public record RawPacket(String type, ByteBuf data) {

    /**
     * pack a packet from {@link RawPacket}
     * @param packet packet
     * @return bytebuf
     */
    public static ByteBuf serialize(RawPacket packet) {

        return BufferBuilder.fromNBT(null, 50);
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
}
