package com.flybirdstudio.cubecraft.net;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.event.EventListener;
import com.flybirdstudio.util.net.Packet;

public class INetHandler implements EventListener {
    protected final IWorld world;
    private ArrayQueue<Packet> sendQueue;

    public INetHandler(IWorld world) {
        this.world = world;
    }

    protected final void send(Packet pkt){
        this.sendQueue.add(pkt);
    }

    public void setSendQueue(ArrayQueue<Packet> sendQueue) {
        this.sendQueue = sendQueue;
    }
}
