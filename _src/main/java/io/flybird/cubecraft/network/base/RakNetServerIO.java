package io.flybird.cubecraft.network.base;

import com.whirvis.jraknet.RakNetException;
import com.whirvis.jraknet.server.RakNetServer;
import io.flybird.cubecraft.network.listener.RakNetServerListenerAdapter;
import io.flybird.cubecraft.network.listener.ServerListenerAdapter;
import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.server.ServerRegistries;

import java.net.InetSocketAddress;


public class RakNetServerIO implements ServerIO {
    private final RakNetServerListenerAdapter listener;
    private RakNetServer server;

    public RakNetServerIO() {
        this.listener = new RakNetServerListenerAdapter(Registries.PACKET, ServerRegistries.NET_HANDLER);
    }

    public RakNetServer getServer() {
        return server;
    }

    @Override
    public void start(int port, int maxConn) {
        this.server = new RakNetServer(port, maxConn);
        this.server.addListener(listener);
        try {
            server.start();
        } catch (RakNetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServerListenerAdapter getListener() {
        return listener;
    }

    @Override
    public void sendPacket(Packet pkt, InetSocketAddress address) {
        this.getListener().sendPacket(pkt, address);
    }

    @Override
    public void closeConnection(InetSocketAddress address) {
        this.getListener().disconnect(address);
    }

    @Override
    public void broadcastPacket(Packet pkt) {
        this.getListener().broadCastPacket(pkt);
    }

    @Override
    public void allCloseConnection() {
        this.getListener().allCloseConnection();
    }
}
