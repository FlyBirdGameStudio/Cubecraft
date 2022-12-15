package io.flybird.util.network.packet;

import io.flybird.util.event.Event;
import io.netty.buffer.ByteBuf;

public interface Packet extends Event {

    void writePacketData(ByteBuf buffer);

    void readPacketData(ByteBuf buffer);
}
