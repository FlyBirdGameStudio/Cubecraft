package io.flybird.cubecraft.internal.net.packet.playing;

import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.file.NBTBuilder;
import io.flybird.util.file.nbt.NBTTagCompound;
import io.netty.buffer.ByteBuf;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@TypeItem("cubecraft:chunk_data")
public class PacketChunkData implements Packet {
    private final Chunk chunk;

    public PacketChunkData(Chunk chunk) {
        this.chunk = chunk;
    }

    @PacketConstructor
    public PacketChunkData(){
        this.chunk=new Chunk(null,new ChunkPos(0,0,0));
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
            GZIPOutputStream g=new GZIPOutputStream(out);
            NBTBuilder.write(chunk.getData(),new DataOutputStream(g));
            g.close();
            BufferUtil.writeArray(out.toByteArray(),buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        try {
            byte[] arr=new byte[buffer.readInt()];
            buffer.readBytes(arr);
            GZIPInputStream in=new GZIPInputStream(new ByteArrayInputStream(arr));
            this.chunk.setData((NBTTagCompound) NBTBuilder.read(new DataInputStream(in)));
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Chunk getChunk() {
        return chunk;
    }
}
