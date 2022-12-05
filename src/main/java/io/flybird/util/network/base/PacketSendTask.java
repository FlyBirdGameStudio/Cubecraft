package io.flybird.util.network.base;


import io.flybird.util.network.event.ChannelTimingEvent;
import io.flybird.util.network.packet.Packet;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.namespace.TypeItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class PacketSendTask implements Runnable {
    private final ArrayQueue<Packet> sending;
    private final ChannelHandlerContext ctx;

    public PacketSendTask(ArrayQueue<Packet> sending, ChannelHandlerContext ctx) {
        this.sending = sending;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        for (Packet packet : this.sending.pollAll(this.sending.size())) {
            ByteBuf out = ByteBufAllocator.DEFAULT.ioBuffer(512, 4194304);
            String type = packet.getClass().getAnnotation(TypeItem.class).value();
            byte[] head = type.getBytes(StandardCharsets.UTF_8);
            out.writeByte(head.length);
            out.writeBytes(head);
            packet.writePacketData(out);
            ctx.writeAndFlush(out);
        }
        ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
    }

}
