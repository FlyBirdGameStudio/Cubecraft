package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.net.base.ServerNettyPipeline;
import io.flybird.cubecraft.net.clientPacket.transfer.PacketBlockChange;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;

import io.flybird.util.event.EventHandler;

public abstract class ServerNetHandler extends INetHandler {
    protected final CubecraftServer server;
    protected ServerNettyPipeline pipeline;

    public ServerNetHandler(CubecraftServer server) {
        this.server = server;
    }

    public void setPipeline(ServerNettyPipeline pipeline) {
        this.pipeline = pipeline;
    }



    //world event handle
    @EventHandler
    public void onBlockChanged(BlockChangeEvent event) {
        this.send(new PacketBlockChange(event.x(), event.y(), event.z(), event.newBlockState()));
    }
}
