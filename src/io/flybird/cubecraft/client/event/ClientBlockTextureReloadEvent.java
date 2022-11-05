package io.flybird.cubecraft.client.event;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.util.event.Event;

import java.util.ArrayList;

public record ClientBlockTextureReloadEvent(Cubecraft client, ResourceManager resourceManager, ArrayList<ResourceLocation> textureList) implements Event {
}
