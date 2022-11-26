package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.clientPacket.Packet;
import io.netty.buffer.ByteBuf;

public class ClientPacketChunkGetRequest implements Packet {
    private long chunkX;
    private long chunkY;
    private long chunkZ;

    public ClientPacketChunkGetRequest(long chunkX, long chunkY, long chunkZ){
        this.chunkX=chunkX;
        this.chunkY=chunkY;
        this.chunkZ=chunkZ;
    }

    //todo:entity uuid

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(getChunkX()).
                writeLong(getChunkY()).
                writeLong(getChunkZ());
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        chunkX =buffer.readLong();
        chunkY =buffer.readLong();
        chunkZ =buffer.readLong();
    }

    public long getChunkX() {
        return chunkX;
    }

    public long getChunkY() {
        return chunkY;
    }

    public long getChunkZ() {
        return chunkZ;
    }

}
