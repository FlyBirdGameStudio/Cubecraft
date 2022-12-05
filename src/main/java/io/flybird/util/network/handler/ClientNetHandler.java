package io.flybird.util.network.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.world.IWorld;

public abstract class ClientNetHandler implements INetHandler {
    protected IWorld world;
    protected final Cubecraft client;

    public ClientNetHandler(Cubecraft client) {
        this.world = client.getClientWorld();
        this.client = client;
    }

    protected final void sendPacket(Packet pkt){
        this.client.getClientIO().getHandler().pushSend(pkt);
    }

    public void setWorld(IWorld clientWorld) {
        this.world=clientWorld;
    }
}


