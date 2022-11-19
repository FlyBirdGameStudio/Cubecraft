package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.net.Packet;
import io.netty.buffer.ByteBuf;

public class ClientPacketChunkLoadRequest implements Packet {
    private String uuid;
    private long ChunkX;
    private long ChunkY;
    private long ChunkZ;
    private ChunkLoadTicket chunkLoadTicket;

    public ClientPacketChunkLoadRequest(String uuid, long chunkX, long chunkY, long chunkZ, ChunkLoadTicket chunkLoadTicket) {
        this.uuid = uuid;
        ChunkX = chunkX;
        ChunkY = chunkY;
        ChunkZ = chunkZ;
        this.chunkLoadTicket = chunkLoadTicket;
    }

    @Override
    public String getType() {
        return "cubecraft:chunk_load_request";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        //todo:packet
    }

    @Override
    public void readPacketData(ByteBuf buffer) {

    }

    public String getUuid() {
        return uuid;
    }

    public long getChunkX() {
        return ChunkX;
    }

    public long getChunkY() {
        return ChunkY;
    }

    public long getChunkZ() {
        return ChunkZ;
    }

    public ChunkLoadTicket getChunkLoadTicket() {
        return chunkLoadTicket;
    }
}
