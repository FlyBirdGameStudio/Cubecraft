package io.flybird.cubecraft.internal.net.packet.connect;

import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.cubecraft.network.packet.PacketConstructor;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * a msg packet with no data carried
 */
@TypeItem("cubecraft:join_response")
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
        this.reason=new String(BufferUtil.unwrap(buffer), StandardCharsets.UTF_8);
    }

    public boolean isAccepted(){
        return Objects.equals(reason, "__ACCEPT__");
    }

    public String getReason() {
        return reason;
    }
}
