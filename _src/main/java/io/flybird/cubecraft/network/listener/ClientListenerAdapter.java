package io.flybird.cubecraft.network.listener;

import io.flybird.cubecraft.client.network.ClientNetHandler;
import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;

import java.net.InetSocketAddress;

public abstract class ClientListenerAdapter extends NetworkListenerAdapter {

    /**
     * an adapter of handler.
     *
     * @param packetConstructor packet registry.
     * @param handlerRegistry
     * @param attachEventBus
     */
    public ClientListenerAdapter(NameSpacedConstructingMap<Packet> packetConstructor, NameSpacedConstructingMap<ClientNetHandler> handlerRegistry) {
        super(packetConstructor);
        for (ClientNetHandler handler:handlerRegistry.createAll().values()){
            this.registerEventListener(handler);
        }
    }

    @Override
    public void disconnect(InetSocketAddress address) {
        this.disconnect();
    }

    @Override
    public void sendPacket(Packet pkt, InetSocketAddress address) {
        this.sendPacket(pkt);
    }

    public abstract void sendPacket(Packet pkt);

    public abstract void disconnect();
}