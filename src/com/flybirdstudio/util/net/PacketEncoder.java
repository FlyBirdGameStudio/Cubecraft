package com.flybirdstudio.util.net;

public interface PacketEncoder<T extends Packet> {
    byte[] encode(T packet);
}
