package io.flybird.cubecraft.internal.net.packet.playing;

import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;

@TypeItem("cubecraft:chunk_load")
public class PacketChunkLoad implements Packet {
    private String world;
    private long chunkX;
    private long chunkY;
    private long chunkZ;
    private ChunkLoadTicket ticket = new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 514);

    public PacketChunkLoad(String world, long chunkX, long chunkY, long chunkZ, ChunkLoadTicket ticket) {
        this.world = world;
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
        BufferUtil.writeString(this.world,buffer);
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        this.chunkX = buffer.readLong();
        this.chunkY = buffer.readLong();
        this.chunkZ = buffer.readLong();
        this.ticket.setTime(buffer.readInt());
        this.ticket.setChunkLoadLevel(ChunkLoadLevel.fromOrder(buffer.readByte()));
        this.world= BufferUtil.readString(buffer);
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

    public String getWorld() {
        return world;
    }
}
