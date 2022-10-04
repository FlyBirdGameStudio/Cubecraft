package com.flybirdstudio.cubecraft.client.event;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.util.event.Event;

public record ClientResourceReloadEvent(Cubecraft client,ResourceManager resourceManager) implements Event {
}
