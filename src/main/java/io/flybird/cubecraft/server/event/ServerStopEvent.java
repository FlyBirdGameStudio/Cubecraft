package io.flybird.cubecraft.server.event;

import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.util.event.Event;

public record ServerStopEvent(CubecraftServer server,String reason) implements Event {
}
