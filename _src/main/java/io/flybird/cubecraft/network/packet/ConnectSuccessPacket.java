package io.flybird.cubecraft.network.packet;

import io.flybird.util.event.Event;

import java.net.InetSocketAddress;

public record ConnectSuccessPacket(InetSocketAddress address) implements Event {

}
