package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.internal.net.packet.playing.*;
import io.flybird.util.network.NetHandlerContext;
import io.flybird.util.network.packet.PacketEventHandler;
import io.flybird.util.network.handler.ServerNetHandler;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.chunk.ChunkLoadLevel;
import io.flybird.cubecraft.world.chunk.ChunkLoadTicket;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.util.event.EventHandler;
import org.joml.Vector3d;

//todo:multi world
public class ServerHandlerPlayerPlaying extends ServerNetHandler {
    public ServerHandlerPlayerPlaying(CubecraftServer server) {
        super(server);
    }


    //client pack event handle
    @PacketEventHandler
    public void onPacketBlockChange(PacketBlockChange packet, NetHandlerContext ctx) {
        this.server.getDim(packet.getWorld()).setBlockState(packet.getX(), packet.getY(), packet.getZ(), packet.getState());
    }

    @PacketEventHandler
    public void onPacketAttack(PacketAttack packet, NetHandlerContext ctx) {
        Entity e = this.server.getEntity(packet.getUuid0());
        Entity e2 = this.server.getEntity(packet.getUuid1());
        if(e==null||e2==null){
            return;
        }
        if (new Vector3d(e.x, e.y, e.z).distance(new Vector3d(e2.x, e2.y, e2.z)) > e.getReachDistance()) {
            return;
        }
        //todo:e2扣血
    }

    @PacketEventHandler
    public void onPacketPositionChange(PacketEntityPosition packet, NetHandlerContext ctx) {
        Entity e=this.server.getEntity(packet.getUuid());
        if(e!=null){
            e.setLocation(packet.getNewLoc(),this.server.getLevel().getDims());
        }
    }

    @PacketEventHandler
    public void onPacketChunkLoadRequest(PacketChunkLoad packet, NetHandlerContext ctx) {
        IWorld dim= this.server.getDim(packet.getWorld());
        if(dim==null){
            System.out.println("dim:"+packet.getWorld());
            return;
        }
        dim.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), packet.getTicket());
    }

    @PacketEventHandler
    public void onPacketChunkGetRequest(PacketChunkGet packet, NetHandlerContext ctx) {
        IWorld dim= this.server.getDim(packet.getWorld());
        if(dim==null){
            System.out.println("dim:"+packet.getWorld());
            return;
        }
        dim.loadChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ(), new ChunkLoadTicket(ChunkLoadLevel.None_TICKING, 10));
        ctx.sendPacket(new PacketChunkData(dim.getChunk(packet.getChunkX(), packet.getChunkY(), packet.getChunkZ())));
    }

    @EventHandler
    public void onBlockChanged(BlockChangeEvent event) {
        this.broadCastPacket(new PacketBlockChange(event.x(), event.y(), event.z(), event.world().getID(), event.newBlockState()));
    }

}
