package com.flybirdstudio.util.net;

import com.flybirdstudio.util.LoopTickingApplication;
import com.flybirdstudio.util.timer.Timer;

public class UDPSocketThread extends LoopTickingApplication {

    private final UDPSocket socket;

    public UDPSocketThread(UDPSocket socket, float speed) {
        this.socket = socket;
        this.timer=new Timer(speed);
    }

    @Override
    public void longTick() {
        this.socket.tick();
    }
}
