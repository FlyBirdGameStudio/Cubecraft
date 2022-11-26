package io.flybird.cubecraft.net.handler;

import io.flybird.cubecraft.net.NetHandlerContext;
import io.flybird.cubecraft.net.PacketEventHandler;
import io.flybird.cubecraft.net.clientPacket.connect.*;
import io.flybird.cubecraft.register.Registry;
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
            if(Registry.getSessionServiceMap().get(packet.getSession().getName()).validSession(packet.getSession())){
                ctx.sendPacket(new PacketPlayerJoinResponse("__ACCEPT__"));
                String uid=Registry.getSessionServiceMap().get(packet.getSession().getName()).genUUID(packet.getSession());
                this.server.getPlayers().add(new Player(null,packet.getSession()),uid,ctx.from());
            }else{
                ctx.sendPacket(new PacketPlayerJoinResponse("join.refuse.session_invalid"));
                ctx.closeConnection();
            }
        }catch (RuntimeException e){
            ctx.sendPacket(new PacketPlayerJoinResponse("join.refuse.no_session_type"));
            ctx.closeConnection();
        }
    }

    @PacketEventHandler
    public void onLeaveRequest(PacketPlayerLeave packet,NetHandlerContext ctx){
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
