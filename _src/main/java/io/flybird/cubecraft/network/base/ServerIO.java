package io.flybird.cubecraft.network.base;

import io.flybird.cubecraft.network.listener.ServerListenerAdapter;
import io.flybird.cubecraft.network.packet.Packet;

import java.net.InetSocketAddress;

public interface ServerIO {
    ServerListenerAdapter getListener();

    void sendPacket(Packet pkt, InetSocketAddress address);

    void closeConnection(InetSocketAddress address);

    void broadcastPacket(Packet pkt);

    void allCloseConnection();

    void start(int port,int maxConnection);
}
