package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.net.Packet;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.event.EventListener;

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
