package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.cubecraft.net.clientPacket.transfer.*;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.world.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.world.event.world.ChunkLoadEvent;
import io.flybird.util.event.EventHandler;

public class ClientNetHandlerPlaying extends ClientNetHandler {
    public ClientNetHandlerPlaying(IWorld world, Cubecraft client) {
        super(world, client);
    }

    @PacketEventHandler
    public void onPacketChunkLoadResponse(PacketChunkData packet, NetHandlerContext ctx) {
        this.client.getClientWorld().setChunk(packet.getChunk());
    }

    @PacketEventHandler
    public void onPacketBlockChanged(PacketBlockChange packet, NetHandlerContext ctx) {
        this.client.getClientWorld().setBlockState(packet.getX(), packet.getY(), packet.getY(), packet.getState());
    }

    public void clientLocationUpdate(PacketEntityPosition)



    @EventHandler
    public void onBlockChanged(BlockChangeEvent e) {
        this.send(new PacketBlockChange(e.x(), e.y(), e.z(), e.newBlockState()));
    }

    @EventHandler
    public void onPlayerAttack(EntityAttackEvent e) {
        this.send(new ClientPacketAttack(e.from().getUID(), e.target().getUID()));
    }

    @EventHandler
    public void onPlayerMove(EntityMoveEvent e) {
        this.send(new PacketEntityPosition(e.e().getUID(), e.oldLocation()));
    }

    @EventHandler
    public void onClientWorldChunkLoad(ChunkLoadEvent e) {
        this.pipeline(new ClientPacketChunkGetRequest(e.cx(), e.cy(), e.cz()));
        this.send(new PacketChunkLoad(e.cx(), e.cy(), e.cz(), e.ticket()));
    }
}
