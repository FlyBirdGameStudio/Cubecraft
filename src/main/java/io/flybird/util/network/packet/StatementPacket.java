package io.flybird.util.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * a statement or signal packet that carries no data.
 */
public abstract class StatementPacket implements Packet{
    @Override
    public void writePacketData(ByteBuf buffer){}

    @Override
    public void readPacketData(ByteBuf buffer){}

    @PacketConstructor
    public StatementPacket() {
    }
}
