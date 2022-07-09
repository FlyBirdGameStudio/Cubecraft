package com.sunrisestudio.cubecraft.net.netHandler;

import com.sunrisestudio.cubecraft.event.EventHandler;
import com.sunrisestudio.cubecraft.event.block.BlockChangeEvent;
import com.sunrisestudio.cubecraft.event.entity.*;
import com.sunrisestudio.cubecraft.net.NetHandler;
import com.sunrisestudio.cubecraft.net.packet.PacketHandler;
import com.sunrisestudio.cubecraft.net.packet.client.*;

public class EntityHandler extends NetHandler {
    @EventHandler
    public void onEntityMove(EntityMoveEvent e){
        this.sendPacket(new PacketClientPositionChange(e.newLocation()));
    }





    @EventHandler
    public void onEntityChat(EntityChatEvent e){
        this.sendPacket(new PacketClientMessage(e.sender(),e.message()));
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEntityEvent e){
        this.sendPacket(new PacketClientInteractEntity(e.From(),e.to()));
    }

    @EventHandler
    public void onEntityAttack(EntityAttackEvent e){
        this.sendPacket(new PacketClientAttack(e.from(),e.target()));
    }

    @EventHandler
    public void onEntityAttack(BlockChangeEvent e){
        this.sendPacket(new PacketClientBlockChange(e.newBlock()));
    }



    @PacketHandler(handledType = "")
    public void onDataPackReceived(){

    }
}
