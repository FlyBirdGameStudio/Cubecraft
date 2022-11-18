package io.flybird.cubecraft.net.serverPacket;

import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.flybird.util.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerPacketBlockChange implements Packet {
    private long x,y,z;
    private BlockState bs;

    public ServerPacketBlockChange(long x, long y, long z, BlockState bs) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.bs = bs;
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
            nbt=NBTBuilder.write(bs.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.writeLong(x).writeLong(y).writeLong(z).writeInt(nbt.capacity()).writeBytes(nbt);
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        x=buffer.readLong();
        y=buffer.readLong();
        z=buffer.readLong();
        int length=buffer.readInt();
        try {
            bs.setData((NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buffer.readBytes(length))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}