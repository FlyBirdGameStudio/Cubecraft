package io.flybird.util.net;

import io.flybird.util.LoopTickingApplication;
import io.flybird.util.timer.Timer;

public class UDPSocketThread extends LoopTickingApplication {

    private final UDPSocket socket;

    public UDPSocketThread(UDPSocket socket, float speed) {
        this.socket = socket;
        this.timer=new Timer(speed);
    }

    @Override
    public void tick() {
        this.socket.tick();
    }
}
