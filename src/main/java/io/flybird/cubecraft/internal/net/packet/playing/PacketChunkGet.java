package io.flybird.cubecraft.internal.net.packet.playing;

import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;

@TypeItem("cubecraft:chunk_get")
public class PacketChunkGet implements Packet {
    private long chunkX;
    private long chunkY;
    private long chunkZ;
    private String world="cubecraft:overworld";

    public PacketChunkGet(long chunkX, long chunkY, long chunkZ,String world){
        this.chunkX=chunkX;
        this.chunkY=chunkY;
        this.chunkZ=chunkZ;
        this.world=world;
    }

    @PacketConstructor
    public PacketChunkGet(){
    }


    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(getChunkX()).
                writeLong(getChunkY()).
                writeLong(getChunkZ());
        BufferUtil.writeString(this.world,buffer);
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        this.chunkX =buffer.readLong();
        this.chunkY =buffer.readLong();
        this.chunkZ =buffer.readLong();
        this.world= BufferUtil.readString(buffer);
    }

    public long getChunkX() {
        return this.chunkX;
    }

    public long getChunkY() {
        return this.chunkY;
    }

    public long getChunkZ() {
        return this.chunkZ;
    }

    public String getWorld() {
        return world;
    }
}
