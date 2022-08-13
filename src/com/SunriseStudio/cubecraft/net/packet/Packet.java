package com.sunrisestudio.cubecraft.net.packet;

import com.sunrisestudio.util.event.TypeGettableEventItem;
import io.netty.buffer.ByteBuf;

public interface Packet extends TypeGettableEventItem {
    String getType();
    ByteBuf serialize();

}
