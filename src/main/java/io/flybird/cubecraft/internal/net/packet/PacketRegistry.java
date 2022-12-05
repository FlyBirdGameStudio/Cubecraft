package io.flybird.cubecraft.internal.net.packet;

import io.flybird.cubecraft.internal.net.packet.connect.*;
import io.flybird.cubecraft.internal.net.packet.playing.*;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.namespace.ItemRegisterFunc;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;

public class PacketRegistry {
    @ItemRegisterFunc
    public void registerPacketConnection(NameSpacedConstructingMap<Packet> map) {
        map.registerItem(PacketPlayerJoinRequest.class);
        map.registerItem(PacketPlayerJoinResponse.class);
        map.registerItem(PacketPlayerKicked.class);
        map.registerItem(PacketPlayerLeave.class);
        map.registerItem(PacketPlayerJoinWorld.class);
        map.registerItem(PacketPlayerJoinWorldResponse.class);
    }

    @ItemRegisterFunc
    public void registerPacketPlying(NameSpacedConstructingMap<Packet> map) {
        map.registerItem(PacketBlockChange.class);
        map.registerItem(PacketAttack.class);
        map.registerItem(PacketChunkData.class);
        map.registerItem(PacketChunkGet.class);
        map.registerItem(PacketChunkLoad.class);
        map.registerItem(PacketEntityPosition.class);
    }
}
