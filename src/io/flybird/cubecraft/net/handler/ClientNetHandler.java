package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.util.event.EventHandler;
import io.flybird.cubecraft.event.block.BlockChangeEvent;
import io.flybird.cubecraft.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.event.world.ChunkLoadEvent;
import io.flybird.cubecraft.net.serverPacket.ServerPacketBlockChange;
import io.flybird.cubecraft.net.serverPacket.ServerPacketChunkGetResponse;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.net.clientPacket.*;

public class ClientNetHandler extends INetHandler{
    private String accessToken;
    final Cubecraft client;
    public ClientNetHandler(IWorld world, Cubecraft client) {
        super(world);
        this.client = client;
    }

    @PacketEventHandler
    public void onPacketChunkLoadResponse(ServerPacketChunkGetResponse packet, NetHandlerContext ctx){
        this.world.setChunk(packet.getChunk());
    }

    @PacketEventHandler
    public void onPacketBlockChanged(ServerPacketBlockChange packet, NetHandlerContext ctx){
        this.world.setBlockState(packet.getX(),packet.getY(), packet.getY(),packet.getState());
    }


    //world event
    @EventHandler
    public void onBlockChanged(BlockChangeEvent e){
        this.send(new ClientPacketBlockChange(accessToken,client.getPlayer().getUID(),e.x(),e.y(),e.z(),e.newBlockState()));
    }

    @EventHandler
    public void onPlayerAttack(EntityAttackEvent e){
        this.send(new ClientPacketAttack(accessToken,e.from().getUID(),e.target().getUID()));
    }

    @EventHandler
    public void onPlayerMove(EntityMoveEvent e){
        this.send(new EntityPositionUpdate(accessToken,e.e().getUID(),e.newLocation(),e.oldLocation()));
    }

    @EventHandler
    public void onClientWorldChunkLoad(ChunkLoadEvent e){
        this.send(new ClientPacketChunkGetRequest(accessToken,e.cx(),e.cy(),e.cz()));
        this.send(new ClientPacketChunkLoadRequest(accessToken,e.cx(),e.cy(),e.cz(),e.ticket()));
    }
}


