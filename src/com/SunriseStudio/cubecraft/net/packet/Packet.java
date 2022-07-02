package com.SunriseStudio.cubecraft.net.packet;

import io.netty.buffer.ByteBuf;

public interface Packet {
    String getType();
    ByteBuf serialize();
}
