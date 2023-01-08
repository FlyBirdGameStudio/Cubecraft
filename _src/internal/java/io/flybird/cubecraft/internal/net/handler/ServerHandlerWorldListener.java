package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.internal.net.packet.playing.PacketBlockChange;
import io.flybird.cubecraft.internal.net.packet.playing.PacketEntityPosition;
import io.flybird.cubecraft.internal.type.NetHandlerType;
import io.flybird.cubecraft.server.net.ServerNetHandler;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.event.entity.EntityMoveEvent;
import io.flybird.util.container.namespace.TypeItem;
import io.flybird.util.event.EventHandler;

@TypeItem(NetHandlerType.SERVER_WORLD_LISTENER)
public class ServerHandlerWorldListener extends ServerNetHandler {

    @EventHandler
    public void onBlockChanged(BlockChangeEvent event) {
        this.broadcastPacket(new PacketBlockChange(event.x(), event.y(), event.z(), event.world().getID(), event.newBlockState()));
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent e) {
        this.broadcastPacket(new PacketEntityPosition(e.e().getUID(),e.newLocation()));
    }
}
