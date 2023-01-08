package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.client.network.ClientNetHandler;
import io.flybird.cubecraft.internal.net.packet.playing.*;
import io.flybird.cubecraft.internal.type.NetHandlerType;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.world.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.world.event.world.ChunkLoadEvent;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.event.EventHandler;

@TypeItem(NetHandlerType.CLIENT_WORLD_LISTENER)
public class ClientHandlerWorldListener extends ClientNetHandler {


    @EventHandler
    public void onPlayerAttack(EntityAttackEvent e) {
        this.sendPacket(new PacketAttack(e.from().getUID(), e.target().getUID()));
    }

    @EventHandler
    public void onBlockChanged(BlockChangeEvent e) {
        this.sendPacket(new PacketBlockChange(e.x(), e.y(), e.z(), Registries.CLIENT.getClientWorld().getID(), e.newBlockState()));
    }

    @EventHandler
    public void onClientWorldChunkLoad(ChunkLoadEvent e) {
        this.sendPacket(new PacketChunkGet(e.pos(), Registries.CLIENT.getClientWorld().getID()));
        this.sendPacket(new PacketChunkLoad(Registries.CLIENT.getClientWorld().getID(), e.pos(), e.ticket()));
    }

    @EventHandler
    public void onPlayerMove(EntityMoveEvent e) {
        this.sendPacket(new PacketEntityPosition(e.e().getUID(), e.oldLocation()));
    }
}
