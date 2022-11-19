package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.net.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ClientPacketAttack implements Packet {
    public String token,uuid0,uuid1;

    public ClientPacketAttack(String token, String uuid0, String uuid1) {
        this.token = token;
        this.uuid0 = uuid0;
        this.uuid1 = uuid1;
    }

    public ClientPacketAttack(){}

    @Override
    public String getType() {
        return "cubecraft:client_packet_attack";
    }

    @Override
    public void writePacketData(ByteBuf buffer) {
        String data="%s/%s/%s".formatted(token,uuid0,uuid1);
        buffer.writeBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void readPacketData(ByteBuf buffer) {
        String[] data=new String(buffer.array(),StandardCharsets.UTF_8).split("/");
        this.token=data[0];
        this.uuid0=data[1];
        this.uuid1=data[2];
    }
}
