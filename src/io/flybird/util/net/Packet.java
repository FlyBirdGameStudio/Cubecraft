package io.flybird.util.net;

import io.flybird.util.event.Event;

public interface Packet extends Event {
    String getType();
}
