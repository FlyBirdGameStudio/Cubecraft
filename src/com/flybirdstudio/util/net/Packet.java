package com.flybirdstudio.util.net;

import com.flybirdstudio.util.event.Event;

public interface Packet extends Event {
    String getType();
}
