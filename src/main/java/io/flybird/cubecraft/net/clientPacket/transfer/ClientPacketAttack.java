package io.flybird.cubecraft.net.clientPacket.transfer;

import io.flybird.cubecraft.net.PacketConstructor;
import io.flybird.cubecraft.net.clientPacket.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ClientPacketAttack implements Packet {
    String uuid0, uuid1;

    public ClientPacketAttack(String uuid0, String uuid1) {
        this.uuid0 = uuid0;
        this.uuid1 = uuid1;
    }

    @PacketConstructor
    public ClientPacketAttack() {
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        String data = "%s/%s".formatted(uuid0, uuid1);
        buffer.writeBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        String[] data = new String(buffer.array(), StandardCharsets.UTF_8).split("/");
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
