package io.flybird.cubecraft.network.packet;

import io.flybird.util.event.Event;

import java.util.Properties;

public record PingPacket(Properties properties) implements Event {}
