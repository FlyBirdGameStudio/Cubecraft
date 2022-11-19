package io.flybird.cubecraft.net.serverPacket;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.flybird.cubecraft.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

public class ServerPacketBlockChange implements Packet {
    private long x;
    private long y;
    private long z;
    private BlockState state;

    public ServerPacketBlockChange(long x, long y, long z, BlockState bs) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.state = bs;
    }

    public ServerPacketBlockChange(){
        this(0,0,0,null);
    }

    @Override
    public String getType() {
        return "cubecraft:server_block_change";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        ByteBuf nbt;
        try {
            nbt=NBTBuilder.write(getState().getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.writeLong(getX()).writeLong(getY()).writeLong(getZ()).writeInt(nbt.capacity()).writeBytes(nbt);
        nbt.release();
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        x=buffer.readLong();
        y=buffer.readLong();
        z=buffer.readLong();
        int length=buffer.readInt();
        ByteBuf buf=buffer.readBytes(length);
        try {
            getState().setData((NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buf)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buf.release();
    }

    public BlockState getState() {
        return state;
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


}