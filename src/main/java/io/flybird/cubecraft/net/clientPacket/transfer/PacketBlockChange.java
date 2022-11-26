package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 * this packet hold a block change event to server,
 * use token access.
 */
public class PacketBlockChange implements Packet {
    private long x;
    private long y;
    private long z;
    private BlockState state;

    public PacketBlockChange(long x, long y, long z, BlockState state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.state = state;
    }

    @PacketConstructor
    public PacketBlockChange(){}

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(x).writeLong(y).writeLong(z);
        try {
            NBTBuilder.write(this.state.getData(),new ByteBufOutputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        x=buffer.readLong();
        y=buffer.readLong();
        z=buffer.readLong();

        NBTTagCompound tag;
        try {
            tag = (NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.state.setData(tag);
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public BlockState getState() {
        return state;
    }
}
