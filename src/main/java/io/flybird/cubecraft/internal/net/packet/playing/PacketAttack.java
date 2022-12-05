package io.flybird.cubecraft.internal.net.packet.playing;

import io.flybird.util.network.packet.PacketConstructor;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

@TypeItem("cubecraft:attack")
public class PacketAttack implements Packet {
    String uuid0, uuid1;

    public PacketAttack(String uuid0, String uuid1) {
        this.uuid0 = uuid0;
        this.uuid1 = uuid1;
    }

    @PacketConstructor
    public PacketAttack() {
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        String data = "%s/%s".formatted(uuid0, uuid1);
        buffer.writeBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        String[] data = new String(BufferUtil.unwrap(buffer), StandardCharsets.UTF_8).split("/");
        this.uuid0 = data[1];
        this.uuid1 = data[2];
    }

    public String getUuid0() {
        return uuid0;
    }

    public String getUuid1() {
        return uuid1;
    }
}
