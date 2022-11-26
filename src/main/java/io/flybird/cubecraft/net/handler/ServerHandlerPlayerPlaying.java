package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.cubecraft.net.clientPacket.transfer.*;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public class ServerHandlerPlayerPlaying extends ServerNetHandler {
    protected final IWorld world;

    public ServerHandlerPlayerPlaying(CubecraftServer server, IWorld world) {
        super(server);
        this.world = world;
    }


    //client pack event handle
    @PacketEventHandler
    public void onPacketBlockChange(PacketBlockChange packet, NetHandlerContext ctx) {
        this.world.setBlockState(packet.getX(), packet.getY(), packet.getZ(), packet.getState());
    }

    @PacketEventHandler
    public void onPacketAttack(ClientPacketAttack packet, NetHandlerContext ctx) {
        Entity e = this.world.getEntity(packet.getUuid0());
        Entity e2 = this.world.getEntity(packet.getUuid1());
        if (new Vector3d(e.x, e.y, e.z).distance(new Vector3d(e2.x, e2.y, e2.z)) > e.getReachDistance() || e == null) {
            return;
        }
        //todo:e2扣血
    }

    @PacketEventHandler
    public void onPacketPositionChange(PacketEntityPosition packet, NetHandlerContext ctx) {
        Entity e = this.world.getEntity(packet.getUuid());
        e.setLocation(packet.getNewLoc());
    }

    @PacketEventHandler
    public void onPacketChunkLoadRequest(PacketChunkLoad packet, NetHandlerContext ctx) {
        this.world.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), packet.getTicket());
    }

    @PacketEventHandler
    public void onPacketChunkGetRequest(ClientPacketChunkGetRequest packet, NetHandlerContext ctx) {
        this.world.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 10));
        ctx.sendPacket(new PacketChunkData(this.world.getChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ())));
    }

}
