package io.flybird.cubecraft.net.clientPacket;

import io.flybird.util.net.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
public class ClientPacketChunkGetRequest implements Packet {
    public String token;
    public long cx, cy, cz;

    //todo:entity uuid

    @Override
    public String getType() {
        return "cubecraft:chunk_get_request";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeLong(cx).writeLong(cy).writeLong(cz).writeByte(token.length()).writeBytes(token.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        cx=buffer.readLong();
        cy=buffer.readLong();
        cz=buffer.readLong();
        byte length=buffer.readByte();
        token=new String(buffer.readBytes(length).array(),StandardCharsets.UTF_8);
    }
}
