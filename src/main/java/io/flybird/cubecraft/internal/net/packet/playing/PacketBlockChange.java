package io.flybird.cubecraft.internal.net.packet.playing;

import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 * this packet hold a block change event to server
 */
@TypeItem("cubecraft:block_change")
public class PacketBlockChange implements Packet {
    private long x;
    private long y;
    private long z;
    private String world;
    private BlockState state;

    public PacketBlockChange(long x, long y, long z, String world, BlockState state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.state = state;
    }

    @PacketConstructor
    public PacketBlockChange(){}

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(x).writeLong(y).writeLong(z);
        NBTTagCompound tag=new NBTTagCompound();
        tag.setString("world",this.world);
        tag.setCompoundTag("state",state.getData());
        try {
            NBTBuilder.write(tag,new ByteBufOutputStream(buffer));
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
        this.world=tag.getString("world");
        this.state.setData(tag.getCompoundTag("state"));
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

    public String getWorld() {
        return world;
    }
}
