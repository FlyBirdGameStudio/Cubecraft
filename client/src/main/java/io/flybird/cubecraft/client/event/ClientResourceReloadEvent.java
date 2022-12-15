package io.flybird.cubecraft.client.event;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.util.event.Event;

public record ClientResourceReloadEvent(Cubecraft client, ResourceManager resourceManager) implements Event {
}
