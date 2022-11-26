package io.flybird.util.net;

import io.flybird.cubecraft.net.clientPacket.Packet;

public interface PacketDecoder<T extends Packet> {
    T decode(byte[] bytes);
}
