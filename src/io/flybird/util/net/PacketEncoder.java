package io.flybird.util.net;

import io.flybird.cubecraft.net.Packet;

public interface PacketEncoder<T extends Packet> {
    byte[] encode(T packet);
}
