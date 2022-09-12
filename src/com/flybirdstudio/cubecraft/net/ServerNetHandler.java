package com.flybirdstudio.cubecraft.net;

import com.flybirdstudio.cubecraft.event.block.BlockChangeEvent;
import com.flybirdstudio.cubecraft.net.clientPacket.*;
import com.flybirdstudio.cubecraft.net.serverPacket.ServerPacketBlockChange;
import com.flybirdstudio.cubecraft.net.serverPacket.ServerPacketChunkGetResponse;
import com.flybirdstudio.cubecraft.world.ServerWorld;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadLevel;
import com.flybirdstudio.cubecraft.world.chunk.ChunkLoadTicket;
import com.flybirdstudio.cubecraft.world.entity.Entity;
import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.event.EventHandler;
import com.flybirdstudio.util.net.Packet;
import org.joml.Vector3d;

public class ServerNetHandler extends INetHandler{
    public ServerNetHandler(ServerWorld world) {
        super(world);
    }

    //client pack event handle
    @PacketEventHandler
    public void onPacketBlockChange(ClientPacketBlockChange packet, ArrayQueue<Packet> sendQueue){
        Entity e=this.world.getEntity(packet.uuid());
        if(new Vector3d(packet.x(),packet.y(),packet.z()).distance(new Vector3d(e.x,e.y,e.z))>e.getReachDistance()||e==null){
            sendQueue.add(new ServerPacketBlockChange(packet.x(),packet.y(),packet.z(),world.getBlockState(packet.x(),packet.y(),packet.z())));
        }
        this.world.setBlockState(packet.x(), packet.y(), packet.z(),packet.state());
    }

    @PacketEventHandler
    public void onPacketAttack(ClientPacketAttack packet,ArrayQueue<Packet> sendQueue){
        Entity e=this.world.getEntity(packet.uuid0());
        Entity e2=this.world.getEntity(packet.uuid1());
        if(new Vector3d(e.x,e.y,e.z).distance(new Vector3d(e2.x,e2.y,e2.z))>e.getReachDistance()||e==null){
            return;
        }
        //todo:e2扣血
    }

    @PacketEventHandler
    public void onPacketPositionChange(ClientPacketEntityPositionUpdate packet,ArrayQueue<Packet> sendQueue){
        Entity e=this.world.getEntity(packet.uuid());
        e.setPos(packet.newLoc().x,packet.newLoc().y,packet.newLoc().z);
    }

    @PacketEventHandler
    public void onPacketChunkLoadRequest(ClientPacketChunkLoadRequest packet,ArrayQueue<Packet> sendQueue){
        Entity e=this.world.getEntity(packet.uuid());
        if(new Vector3d(e.x,e.y,e.z).distance(new Vector3d(packet.cx()*16,packet.cy()*16,packet.cz()*16))<512){
            this.world.loadChunk(packet.cx(), packet.cy(), packet.cz(),packet.chunkLoadTicket());
        }
    }

    @PacketEventHandler
    public void onPacketChunkGetRequest(ClientPacketChunkGetRequest packet,ArrayQueue<Packet> sendQueue){
        Entity e=this.world.getEntity(packet.uuid());
        if(new Vector3d(e.x,e.y,e.z).distance(new Vector3d(packet.cx()*16,packet.cy()*16,packet.cz()*16))<512){
            this.world.loadChunk(packet.cx(), packet.cy(), packet.cz(),new ChunkLoadTicket(ChunkLoadLevel.None_TICKING,10));
            sendQueue.add(new ServerPacketChunkGetResponse(this.world.getChunk(packet.cx(),packet.cy(),packet.cz())));
        }
    }


    //world event handle
    @EventHandler
    public void onBlockChanged(BlockChangeEvent event){
        this.send(new ServerPacketBlockChange(event.x(),event.y(),event.z(),event.newBlockState()));
    }
}
