package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinResponse;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinWorld;
import io.flybird.cubecraft.internal.net.packet.connect.PacketPlayerJoinWorldResponse;
import io.flybird.util.network.NetHandlerContext;
import io.flybird.util.network.packet.PacketEventHandler;
import io.flybird.util.network.handler.ClientNetHandler;
import io.flybird.cubecraft.register.ContentRegistry;

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
        this.client.setClientWorld( ContentRegistry.getWorldProviderMap()
                .get(packet.getId())
                .createClientWorld(this.client)
        );
    }
}
