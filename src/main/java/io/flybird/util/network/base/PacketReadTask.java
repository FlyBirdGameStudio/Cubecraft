package io.flybird.util.network.base;

import io.flybird.util.network.NetHandlerContext;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.register.Registry;
import io.flybird.util.logging.LogHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class PacketReadTask implements Runnable {
    private final LogHandler logHandler = LogHandler.create("PacketSendTask-" + this.hashCode());

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
                pkt = Registry.getPackets().create(type);
            } catch (RuntimeException e) {
                this.logHandler.exception("cannot create packet:" + e.getMessage());
                return;
            }
            int length = this.buffer.writerIndex() - this.buffer.readerIndex();
            try {

                pkt.readPacketData(this.buffer);

            } catch (Exception e) {
                this.logHandler.exception(e);
            }
            this.handler.getEventBus().callEvent(pkt, new NetHandlerContext(this.handler, (InetSocketAddress) ctx.channel().remoteAddress(), ctx));
            this.buffer.release();
            new PacketSendTask(this.handler.sending, ctx).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}