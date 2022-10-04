package com.flybirdstudio.cubecraft.client.event;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.util.event.Event;

public record ScreenInitializeEvent(Cubecraft client, Screen screen) implements Event {}
