package io.flybird.cubecraft.client.event;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.util.event.Event;

public record ScreenInitializeEvent(Cubecraft client, Screen screen) implements Event {}
