package io.flybird.starfish3d.event;

import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.platform.input.Mouse;
import io.flybird.util.event.Event;

public record MouseClickEvent(int x, int y, int button) implements Event {
    public int fixedY(){
        return (-y + Display.getHeight());
    }
}
