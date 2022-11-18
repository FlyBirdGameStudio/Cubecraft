package io.flybird.starfish3d.event;

import io.flybird.util.event.Event;

public record KeyReleaseEvent(int key) implements Event {
}
