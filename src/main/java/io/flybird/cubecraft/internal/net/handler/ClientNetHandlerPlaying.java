package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.client.Cubecraft;


import io.flybird.cubecraft.network.NetHandlerContext;
import io.flybird.cubecraft.client.network.ClientNetHandler;
import io.flybird.cubecraft.network.packet.PacketEventHandler;
import io.flybird.cubecraft.internal.net.packet.playing.*;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.world.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.world.event.world.ChunkLoadEvent;
import io.flybird.util.container.CollectionUtil;
import io.flybird.util.event.EventHandler;

import java.util.Objects;

public class ClientNetHandlerPlaying extends ClientNetHandler {
    public ClientNetHandlerPlaying(Cubecraft client) {
        super(client);
    }

    @PacketEventHandler
    public void onPacketChunkLoadResponse(PacketChunkData packet, NetHandlerContext ctx) {
        System.out.println("receive chunk");
        this.client.getClientWorld().setChunk(packet.getChunk());
    }

    @PacketEventHandler
    public void onPacketBlockChanged(PacketBlockChange packet, NetHandlerContext ctx) {
        this.client.getClientWorld().setBlockState(packet.getX(), packet.getY(), packet.getY(), packet.getState());
    }

    @PacketEventHandler
    public void clientLocationUpdate(PacketEntityPosition loc, NetHandlerContext ctx) {
        Entity e=this.client.getClientWorld().getEntity(loc.getUuid());
        if(Objects.equals(loc.getUuid(), this.client.getPlayer().getUID())){
            this.client.setClientWorld( Registries.WORLD_PROVIDER
                    .get(loc.getNewLoc().getDim())
                    .createClientWorld(this.client)
            );
        }else{
            if(e!=null&& Objects.equals(loc.getNewLoc().getDim(), this.client.getClientWorld().getID())){
                e.setLocation(loc.getNewLoc(), CollectionUtil.wrap(this.client.getClientWorld().getID(),this.client.getClientWorld()));
            }
        }
    }


    @EventHandler
    public void onBlockChanged(BlockChangeEvent e) {
        this.sendPacket(new PacketBlockChange(e.x(), e.y(), e.z(), this.client.getClientWorld().getID(), e.newBlockState()));
    }

    @EventHandler
    public void onPlayerAttack(EntityAttackEvent e) {
        this.sendPacket(new PacketAttack(e.from().getUID(), e.target().getUID()));
    }

    @EventHandler
    public void onPlayerMove(EntityMoveEvent e) {
        this.sendPacket(new PacketEntityPosition(e.e().getUID(), e.oldLocation()));
    }

    @EventHandler
    public void onClientWorldChunkLoad(ChunkLoadEvent e) {
        this.client.getClientIO().getHandler().pushSend(new PacketChunkGet(e.cx(), e.cy(), e.cz(),this.world.getID()));
        this.client.getClientIO().getHandler().pushSend(new PacketChunkLoad(this.world.getID(), e.cx(), e.cy(), e.cz(), e.ticket()));
    }
}
