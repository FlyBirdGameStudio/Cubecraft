package io.flybird.cubecraft.network.base;

import io.flybird.cubecraft.network.NetHandlerContext;
import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.cubecraft.register.Registries;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class PacketReadTask implements Runnable {
    private final Logger logHandler = org.apache.logging.log4j.LogManager.getLogger("PacketSendTask-" + this.hashCode());

    private final ByteBuf buffer;
    private final ChannelHandlerContext ctx;
    private final NettyChannelHandler handler;

    public PacketReadTask(ByteBuf buffer, NettyChannelHandler handler, ChannelHandlerContext ctx) {
        this.buffer = buffer;
        this.ctx = ctx;
        this.handler = handler;
    }

    @Override
    public void run() {
        this.buffer.resetReaderIndex();
        byte headSize = this.buffer.readByte();
        byte[] head = new byte[headSize];
        this.buffer.readBytes(head);
        String type = new String(head, StandardCharsets.UTF_8);
        try {
            Packet pkt;
            try {
                pkt = Registries.PACKET.create(type);
            } catch (RuntimeException e) {
                this.logHandler.error("cannot create packet:" + e.getMessage());
                return;
            }
            try {

                pkt.readPacketData(this.buffer);

            } catch (Exception e) {
                this.logHandler.catching(e);
            }
            this.handler.getEventBus().callEvent(pkt, new NetHandlerContext( (InetSocketAddress) ctx.channel().remoteAddress(),null));
            this.buffer.release();
            new PacketSendTask(this.handler.sending, ctx).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
