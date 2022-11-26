package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.clientPacket.Packet;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class PacketChunkData implements Packet {
    private Chunk chunk;

    public PacketChunkData(Chunk chunk) {
        this.chunk = chunk;
    }

    public PacketChunkData(){
        this.chunk=new Chunk(null,new ChunkPos(0,0,0));
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        try {
            NBTBuilder.write(chunk.getData(),new ByteBufOutputStream(buffer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        try {
            this.chunk.setData((NBTTagCompound) NBTBuilder.read(new ByteBufInputStream(buffer)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Chunk getChunk() {
        return chunk;
    }
}
