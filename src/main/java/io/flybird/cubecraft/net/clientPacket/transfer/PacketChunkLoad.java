package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.netty.buffer.ByteBuf;

public class PacketChunkLoad implements Packet {
    private long chunkX;
    private long chunkY;
    private long chunkZ;
    private ChunkLoadTicket ticket = new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 514);

    public PacketChunkLoad(long chunkX, long chunkY, long chunkZ, ChunkLoadTicket ticket) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        this.ticket = ticket;
    }

    @PacketConstructor
    public PacketChunkLoad() {
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(this.chunkX)
                .writeLong(this.chunkY)
                .writeLong(this.chunkZ)
                .writeInt(this.ticket.getTime())
                .writeByte(this.ticket.getChunkLoadLevel().getOrder());
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        this.chunkX = buffer.readLong();
        this.chunkY = buffer.readLong();
        this.chunkZ = buffer.readLong();
        this.ticket.setTime(buffer.readInt());
        this.ticket.setChunkLoadLevel(ChunkLoadLevel.fromOrder(buffer.readByte()));
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

    public ChunkLoadTicket getTicket() {
        return ticket;
    }
}
