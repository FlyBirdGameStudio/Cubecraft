package io.flybird.cubecraft.net.clientPacket.connect;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * a msg packet with no data carried
 */
public class PacketPlayerJoinResponse implements Packet {
    private String reason;

    public PacketPlayerJoinResponse(String reason) {
        this.reason = reason;
    }

    @PacketConstructor
    public PacketPlayerJoinResponse(){}

    @Override
    public void writePacketData(ByteBuf buffer) {
        buffer.writeBytes(this.reason.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        this.reason=new String(buffer.array(), StandardCharsets.UTF_8);
    }

    public boolean isAccepted(){
        return Objects.equals(reason, "__ACCEPT__");
    }

    public String getReason() {
        return reason;
    }
}
