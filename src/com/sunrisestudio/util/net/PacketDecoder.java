package com.sunrisestudio.util.net;

import com.sunrisestudio.cubecraft.net.packet.Packet;

public interface PacketDecoder<T extends Packet> {
    T decode(byte[] bytes);
}
