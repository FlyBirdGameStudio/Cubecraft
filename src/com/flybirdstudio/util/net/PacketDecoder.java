package com.flybirdstudio.util.net;

public interface PacketDecoder<T extends Packet> {
    T decode(byte[] bytes);
}
