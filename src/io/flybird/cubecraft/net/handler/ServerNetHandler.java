package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.event.block.BlockChangeEvent;

import io.flybird.cubecraft.net.Packet;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.cubecraft.net.clientPacket.*;
import io.flybird.cubecraft.net.serverPacket.ServerPacketBlockChange;
import io.flybird.cubecraft.net.serverPacket.ServerPacketChunkGetResponse;
import io.flybird.cubecraft.world.ServerWorld;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.event.EventHandler;
import org.joml.Vector3d;

//todo:token valid
public class ServerNetHandler extends INetHandler {
    public ServerNetHandler(ServerWorld world) {
        super(world);
    }

    //client pack event handle
    @PacketEventHandler
    public void onPacketBlockChange(ClientPacketBlockChange packet, ArrayQueue<Packet> sendQueue) {
        Entity e = this.world.getEntity(packet.uuid);
        if (new Vector3d(packet.x, packet.y, packet.z).distance(new Vector3d(e.x, e.y, e.z)) > e.getReachDistance() || e == null) {
            sendQueue.add(new ServerPacketBlockChange(packet.x, packet.y, packet.z, world.getBlockState(packet.x, packet.y, packet.z)));
        }
        this.world.setBlockState(packet.x, packet.y, packet.z, packet.state);
    }

    @PacketEventHandler
    public void onPacketAttack(ClientPacketAttack packet, ArrayQueue<Packet> sendQueue) {
        Entity e = this.world.getEntity(packet.uuid0);
        Entity e2 = this.world.getEntity(packet.uuid1);
        if (new Vector3d(e.x, e.y, e.z).distance(new Vector3d(e2.x, e2.y, e2.z)) > e.getReachDistance() || e == null) {
            return;
        }
        //todo:e2扣血
    }

    @PacketEventHandler
    public void onPacketPositionChange(EntityPositionUpdate packet, ArrayQueue<Packet> sendQueue) {
        Entity e = this.world.getEntity(packet.getUuid());
        e.setPos(packet.getNewLoc().x, packet.getNewLoc().y, packet.getNewLoc().z);
    }

    @PacketEventHandler
    public void onPacketChunkLoadRequest(ClientPacketChunkLoadRequest packet, ArrayQueue<Packet> sendQueue) {
        Entity e = this.world.getEntity(packet.getUuid());
        if (new Vector3d(e.x, e.y, e.z).distance(new Vector3d(packet.getChunkX() * 16, packet.getChunkY() * 16, packet.getChunkZ() * 16)) < 512) {
            this.world.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), packet.getChunkLoadTicket());
        }
    }

    @PacketEventHandler
    public void onPacketChunkGetRequest(ClientPacketChunkGetRequest packet, ArrayQueue<Packet> sendQueue) {
        this.world.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 10));
        sendQueue.add(new ServerPacketChunkGetResponse(this.world.getChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ())));
    }


    //world event handle
    @EventHandler
    public void onBlockChanged(BlockChangeEvent event) {
        this.send(new ServerPacketBlockChange(event.x(), event.y(), event.z(), event.newBlockState()));
    }
}
