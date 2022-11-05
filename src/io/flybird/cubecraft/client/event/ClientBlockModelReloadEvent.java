package io.flybird.cubecraft.client.event;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.util.event.Event;

import java.util.List;

public record ClientBlockModelReloadEvent(Cubecraft client, ResourceManager resourceManager, List<ResourceLocation> textureList) implements Event {
}
