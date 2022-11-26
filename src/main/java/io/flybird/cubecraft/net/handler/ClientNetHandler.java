package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.cubecraft.net.clientPacket.transfer.*;
import io.flybird.util.event.EventHandler;
import io.flybird.cubecraft.world.event.block.BlockChangeEvent;
import io.flybird.cubecraft.world.event.entity.EntityAttackEvent;
import io.flybird.cubecraft.world.event.entity.EntityMoveEvent;
import io.flybird.cubecraft.world.event.world.ChunkLoadEvent;
import io.flybird.cubecraft.world.IWorld;

public abstract class ClientNetHandler extends INetHandler {
    protected final IWorld world;
    protected final Cubecraft client;

    public ClientNetHandler(IWorld world, Cubecraft client) {
        this.world = world;
        this.client = client;
    }




    //world event



}


