package io.flybird.cubecraft.internal.net.handler;

import io.flybird.cubecraft.auth.SessionService;
import io.flybird.cubecraft.network.NetHandlerContext;
import io.flybird.cubecraft.network.packet.PacketEventHandler;
import io.flybird.cubecraft.internal.net.packet.connect.*;
import io.flybird.cubecraft.client.network.ServerNetHandler;
import io.flybird.cubecraft.register.Registries;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.server.event.*;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.util.event.EventHandler;

/**
 * this handler handles player connection,runs in server side.
 */

public class ServerHandlerPlayerConnection extends ServerNetHandler {
    public ServerHandlerPlayerConnection(CubecraftServer server) {
        super(server);
    }

    @PacketEventHandler
    public void onJoinRequest(PacketPlayerJoinRequest packet, NetHandlerContext ctx){
        try{
            SessionService service= Registries.SESSION_SERVICE.get(packet.getSession().getType());
            if(service.validSession(packet.getSession())){
                ctx.sendPacket(new PacketPlayerJoinResponse("__ACCEPT__"));
                String uid=service.genUUID(packet.getSession());
                Player p=new Player(null,packet.getSession());
                this.server.getPlayers().add(p,uid,ctx.from());
            }else{
                ctx.sendPacket(new PacketPlayerJoinResponse("join.refuse.session_invalid"));
                //ctx.closeConnection();
            }
        }catch (RuntimeException e){
            e.printStackTrace();
            ctx.sendPacket(new PacketPlayerJoinResponse("join.refuse.no_session_type"));
            //ctx.closeConnection();
        }
    }

    @PacketEventHandler
    public void onJoinRequest(PacketPlayerJoinWorld packet, NetHandlerContext ctx){
        ctx.sendPacket(new PacketPlayerJoinWorldResponse(this.server.getLevel().getSpawnPoint("").getDim(),this.server.getLevel().getInfo()));
    }

    @PacketEventHandler
    public void onLeaveRequest(PacketPlayerLeave packet, NetHandlerContext ctx){
        this.server.getEventBus().callEvent(new PlayerLeaveEvent(this.server.getPlayers().getPlayer(ctx.from())));
        ctx.closeConnection();
    }



    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e){
        this.pipeline.getDefaultHandler(this.server.getPlayers().getAddr(e.player())).pushSend(new PacketPlayerKicked(e.reason()));
        this.pipeline.getDefaultHandler(this.server.getPlayers().getAddr(e.player())).close();
    }

    @EventHandler
    public void onServerClosed(ServerStopEvent e){
        for (String uuid:this.server.getPlayers().getUuid2player().keySet()){
            this.pipeline.getDefaultHandler(this.server.getPlayers().getAddr(uuid)).pushSend(new PacketPlayerKicked(e.reason()));
            this.pipeline.getDefaultHandler(this.server.getPlayers().getAddr(uuid)).close();
            this.server.getPlayers().remove(uuid);
        }
    }
}
