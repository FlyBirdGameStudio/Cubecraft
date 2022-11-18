package io.flybird.cubecraft.net;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.event.EventHandler;
import io.flybird.cubecraft.event.block.BlockChangeEvent;
import io.flybird.cubecraft.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.event.world.ChunkLoadEvent;
import io.flybird.cubecraft.net.serverPacket.ServerPacketBlockChange;
import io.flybird.cubecraft.net.serverPacket.ServerPacketChunkGetResponse;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.net.Packet;
import io.flybird.cubecraft.net.clientPacket.*;

public class ClientNetHandler extends INetHandler{
    private String accessToken;


    final Cubecraft client;
    public ClientNetHandler(IWorld world, Cubecraft client) {
        super(world);
        this.client = client;
    }

    @EventHandler
    public void onPacketChunkLoadResponse(ServerPacketChunkGetResponse packet, ArrayQueue<Packet> sendQueue){
        this.world.setChunk(packet.chunk());
    }

    @EventHandler
    public void onPacketBlockChanged(ServerPacketBlockChange packet, ArrayQueue<Packet> sendQueue){
        this.world.setBlockState(packet.x(),packet.y(), packet.z(),packet.bs());
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
        this.send(new ClientPacketEntityPositionUpdate(e.e().getUID(),e.newLocation(),e.oldLocation()));
    }

    @EventHandler
    public void onClientWorldChunkLoad(ChunkLoadEvent e){
        this.send(new ClientPacketChunkGetRequest(e.entity().getUID(),e.cx(),e.cy(),e.cz()));
        this.send(new ClientPacketChunkLoadRequest(e.entity().getUID(),e.cx(),e.cy(),e.cz(),e.ticket()));
    }
}


