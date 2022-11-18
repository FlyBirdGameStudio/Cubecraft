package io.flybird.cubecraft.net.serverPacket;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.util.file.nbt.NBTBuilder;
import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.flybird.util.net.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

public class ServerPacketChunkGetResponse implements Packet {
    private Chunk chunk;

    public ServerPacketChunkGetResponse(Chunk chunk) {
        this.chunk = chunk;
    }

    public ServerPacketChunkGetResponse(){
        this.chunk=new Chunk(null,new ChunkPos(0,0,0));
    }

    @Override
    public String getType() {
        return "cubecraft:chunk_get_response";
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
}
