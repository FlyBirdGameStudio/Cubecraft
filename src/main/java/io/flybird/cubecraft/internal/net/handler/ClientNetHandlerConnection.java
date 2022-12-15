package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinResponse;
import io.flybird.cubecraft.network.NetHandlerContext;
import io.flybird.cubecraft.client.network.ClientNetHandler;
import io.flybird.cubecraft.network.packet.PacketEventHandler;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinWorld;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinWorldResponse;

public class ClientNetHandlerConnection extends ClientNetHandler {
    public ClientNetHandlerConnection(Cubecraft client) {
        super(client);
    }

    @PacketEventHandler
    public void onJoinResponse(PacketPlayerJoinResponse response, NetHandlerContext ctx){
        if(response.isAccepted()){
            ctx.sendPacket(new PacketPlayerJoinWorld());
        }else{
            System.out.println(response.getReason());
            ctx.closeConnection();
        }
    }

    @PacketEventHandler
    public void onJoinResponse(PacketPlayerJoinWorldResponse packet, NetHandlerContext ctx){
        this.client.setClientLevelInfo(packet.getInfo());
        this.client.setClientWorld( Registries.WORLD_PROVIDER
                .get(packet.getId())
                .createClientWorld(this.client)
        );
    }
}
