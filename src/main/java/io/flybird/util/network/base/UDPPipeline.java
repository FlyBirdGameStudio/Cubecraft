package io.flybird.util.network.base;

import io.flybird.util.container.MultiMap;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class UDPPipeline extends AbstractNetworkPipeline {
    private final MultiMap<String,InetSocketAddress> connectionMap = new MultiMap<>();
    private String connectionManifest = "starfish_network";
    private final HashMap<String, ArrayList<Byte>> packetCache = new HashMap<>();
    private final HashMap<String, ArrayList<Integer>> packetCacheL = new HashMap<>();
    private final NameSpacedConstructingMap<Packet> packetRegistry;
    private DatagramSocket socket;
    private int port = 0;

    private ExecutorService eventLoopGroup;

    public UDPPipeline(NameSpacedConstructingMap<Packet> packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    public void init(int thread) {
        this.eventLoopGroup = Executors.newFixedThreadPool(thread * 2);
        try {
            if (this.port == 0) {
                this.socket = new DatagramSocket();
            } else {
                this.socket = new DatagramSocket(this.port);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < thread; i++) {
            this.eventLoopGroup.submit(new UDPReceiveEventLoop(this));
            this.eventLoopGroup.submit(new UDPSenderEventLoop(this));
        }
    }

    public void setConnectionManifest(String connectionManifest) {
        this.connectionManifest = connectionManifest;
    }

    public String getConnectionManifest() {
        return connectionManifest;
    }

    public MultiMap<String,InetSocketAddress> getConnectionMap() {
        return connectionMap;
    }

    public HashMap<String, ArrayList<Byte>> getPacketCache() {
        return packetCache;
    }

    public HashMap<String, ArrayList<Integer>> getPacketCacheL() {
        return packetCacheL;
    }

    public NameSpacedConstructingMap<Packet> getPacketRegistry() {
        return packetRegistry;
    }

    public ExecutorService getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
