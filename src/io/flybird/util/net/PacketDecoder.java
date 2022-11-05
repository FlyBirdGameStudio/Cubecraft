package io.flybird.util.net;

public interface PacketDecoder<T extends Packet> {
    T decode(byte[] bytes);
}
