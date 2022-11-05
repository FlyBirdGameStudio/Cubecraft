package io.flybird.cubecraft.client.event;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.event.Event;

public record ClientShutdownEvent(Cubecraft client) implements Event {

}
