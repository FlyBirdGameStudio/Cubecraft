package io.flybird.starfish3d.platform;

import io.flybird.util.event.Event;

public record KeyPressEvent(int key) implements Event {
}