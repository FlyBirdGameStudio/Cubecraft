package com.sunrisestudio.util.net;

import com.sunrisestudio.cubecraft.net.packet.Packet;

public interface PacketEncoder<T extends Packet> {
    byte[] encode(T packet);
}
