package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.clientPacket.Packet;
import java.net.SocketAddress;

public record PacketSend(Packet pkt, SocketAddress from, SocketAddress to) {
}
