package com.flybirdstudio.cubecraft.net;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.event.EventHandler;
import com.flybirdstudio.cubecraft.event.block.BlockChangeEvent;
import com.flybirdstudio.cubecraft.event.entity.EntityAttackEvent;
import com.flybirdstudio.cubecraft.event.entity.EntityMoveEvent;
import com.flybirdstudio.cubecraft.event.world.ChunkLoadEvent;
import com.flybirdstudio.cubecraft.net.clientPacket.*;
import com.flybirdstudio.cubecraft.net.serverPacket.ServerPacketBlockChange;
import com.flybirdstudio.cubecraft.net.serverPacket.ServerPacketChunkGetResponse;
import com.flybirdstudio.cubecraft.world.IWorld;

public class ClientNetHandler extends INetHandler{
    final Cubecraft client;
    public ClientNetHandler(IWorld world, Cubecraft client) {
        super(world);
        this.client = client;
    }

    @EventHandler
    public void onPacketChunkLoadResponse(ServerPacketChunkGetResponse packet){
        this.world.setChunk(packet.chunk());
    }

    @EventHandler
    public void onPacketBlockChanged(ServerPacketBlockChange packet){
        this.world.setBlockState(packet.x(),packet.y(), packet.z(),packet.bs());
    }


    //world event
    @EventHandler
    public void onBlockChanged(BlockChangeEvent e){
        this.send(new ClientPacketBlockChange(client.getPlayer().getUID(),e.x(),e.y(),e.z(),e.newBlockState()));
    }

    @EventHandler
    public void onPlayerAttack(EntityAttackEvent e){
        this.send(new ClientPacketAttack(e.from().getUID(),e.target().getUID()));
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


