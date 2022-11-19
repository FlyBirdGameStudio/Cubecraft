package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.net.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
public class ClientPacketChunkGetRequest implements Packet {
    private String token;
    private long chunkX;
    private long chunkY;
     private long chunkZ;

    public ClientPacketChunkGetRequest(String token, long chunkX, long chunkY, long chunkZ){
        this.chunkX=chunkX;
        this.chunkY=chunkY;
        this.chunkZ=chunkZ;
        this.token=token;
    }

    //todo:entity uuid

    @Override
    public String getType() {
        return "cubecraft:chunk_get_request";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(getChunkX()).
                writeLong(getChunkY()).
                writeLong(getChunkZ()).
                writeByte(getToken().length())
                .writeBytes(getToken().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        chunkX =buffer.readLong();
        chunkY =buffer.readLong();
        chunkZ =buffer.readLong();
        byte length=buffer.readByte();
        ByteBuf buf=buffer.readBytes(length);
        token=new String(buf.array(),StandardCharsets.UTF_8);
        buf.release();
    }

    public String getToken() {
        return token;
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
