package com.flybirdstudio.cubecraft.client.net.netHandler;

import com.flybirdstudio.util.event.EventHandler;
import com.flybirdstudio.cubecraft.event.block.BlockChangeEvent;
import com.flybirdstudio.cubecraft.event.entity.*;
import com.flybirdstudio.cubecraft.client.net.NetHandler;
import com.flybirdstudio.cubecraft.client.net.packet.PacketHandler;
import com.flybirdstudio.cubecraft.client.net.packet.client.*;

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
        this.sendPacket(new PacketClientBlockChange(e.newBlockState()));
    }



    @PacketHandler(handledType = "")
    public void onDataPackReceived(){

    }
}
