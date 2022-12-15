package io.flybird.starfish3d.event;

import io.flybird.starfish3d.platform.Window;
import io.flybird.util.event.Event;

public record MouseClickEvent(Window window, int x, int y, int button) implements Event {
    public int fixedY(){
        return (-y + window().getWindowHeight());
    }
}
