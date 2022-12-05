package io.flybird.util.network.handler;

import io.flybird.util.network.base.ServerNettyPipeline;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.server.CubecraftServer;

public abstract class ServerNetHandler implements INetHandler {
    protected final CubecraftServer server;
    protected ServerNettyPipeline pipeline;

    public ServerNetHandler(CubecraftServer server) {
        this.server = server;
    }

    public void setPipeline(ServerNettyPipeline pipeline) {
        this.pipeline = pipeline;
    }

    protected void broadCastPacket(Packet packet) {
        for (String uuid:this.server.getPlayers().getUuid2player().keySet()){
            this.pipeline.getDefaultHandler(this.server.getPlayers().getAddr(uuid)).pushSend(packet);
        }
    }
}
