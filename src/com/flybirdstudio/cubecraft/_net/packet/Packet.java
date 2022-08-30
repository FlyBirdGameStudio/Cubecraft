package com.flybirdstudio.cubecraft.client.net.packet;

import io.netty.buffer.ByteBuf;

public interface Packet{
    String getType();
    ByteBuf serialize();

}
