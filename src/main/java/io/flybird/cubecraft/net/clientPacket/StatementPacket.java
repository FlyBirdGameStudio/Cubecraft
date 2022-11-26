package io.flybird.cubecraft.net.clientPacket;

import io.flybird.cubecraft.net.PacketConstructor;
import io.netty.buffer.ByteBuf;

/**
 * a statement or signal packet that carries no data.
 */
public class StatementPacket implements Packet{
    @Override
    public void writePacketData(ByteBuf buffer){};

    @Override
    public void readPacketData(ByteBuf buffer){}

    @PacketConstructor
    public StatementPacket() {
    }
}
