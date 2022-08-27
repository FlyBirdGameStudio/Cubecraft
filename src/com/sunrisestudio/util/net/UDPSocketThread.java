package com.sunrisestudio.util.net;

import com.sunrisestudio.util.LoopTickingApplication;
import com.sunrisestudio.util.timer.Timer;

public class UDPSocketThread extends LoopTickingApplication {

    private final UDPSocket socket;

    public UDPSocketThread(UDPSocket socket,float speed) {
        this.socket = socket;
        this.timer=new Timer(speed);
    }

    @Override
    public void longTick() {
        this.socket.tick();
    }
}
