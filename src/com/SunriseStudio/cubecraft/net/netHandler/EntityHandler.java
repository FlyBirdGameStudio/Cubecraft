package com.SunriseStudio.cubecraft.net.netHandler;

import com.SunriseStudio.cubecraft.event.EventHandler;
import com.SunriseStudio.cubecraft.event.block.BlockChangeEvent;
import com.SunriseStudio.cubecraft.event.entity.*;
import com.SunriseStudio.cubecraft.net.NetHandler;
import com.SunriseStudio.cubecraft.net.packet.client.PacketClientAttack;
import com.SunriseStudio.cubecraft.net.packet.client.PacketClientPositionChange;
import com.SunriseStudio.cubecraft.net.packet.client.PacketClientBlockChange;

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

}
