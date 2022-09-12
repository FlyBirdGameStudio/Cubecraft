package com.flybirdstudio.cubecraft.net.base;

import com.flybirdstudio.cubecraft.net.ChannelTimingEvent;
import com.flybirdstudio.cubecraft.net.INetHandler;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.container.ArrayUtil;
import com.flybirdstudio.util.container.BufferBuilder;
import com.flybirdstudio.util.container.namespace.NameSpacedRegisterMap;
import com.flybirdstudio.util.event.EventListener;
import com.flybirdstudio.util.net.Packet;
import com.flybirdstudio.util.net.PacketDecoder;
import com.flybirdstudio.util.net.PacketEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NettyChannelHandler extends ChannelInboundHandlerAdapter {
    private final NameSpacedRegisterMap<? extends PacketEncoder, ?> packetEncoderRegistry;
    private final NameSpacedRegisterMap<? extends PacketDecoder, ?> packetDecoderRegistry;

    private ChannelHandlerContext ctx;
    Channel ch = null;
    private final ArrayQueue<Packet> sending = new ArrayQueue<>();
    private final int maxSending, sendingspeed;

    public NettyChannelHandler(NameSpacedRegisterMap<? extends PacketEncoder, ?> packetEncoderRegistry, NameSpacedRegisterMap<? extends PacketDecoder, ?> packetDecoderRegistry, int maxSending, int sendingspeed) {
        this.packetEncoderRegistry = packetEncoderRegistry;
        this.packetDecoderRegistry = packetDecoderRegistry;
        this.maxSending = maxSending;
        this.sendingspeed = sendingspeed;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        byte[] data = ((ByteBuf) msg).array();
        byte headSize = data[0];
        String type = new String(ArrayUtil.copySub(1, headSize, data), StandardCharsets.UTF_8);
        Packet pkt = this.packetDecoderRegistry.get(type).decode(data);
        Registery.getNetworkEventBus().callEvent(pkt,sending);
        for (EventListener netHandler:Registery.getNetworkEventBus().getHandlers()){
            ((INetHandler) netHandler).setSendQueue(this.sending);
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.ch = ctx.channel();
        ch.eventLoop().schedule(
                () -> {
                    List<Packet> packets = sending.pollAll(maxSending);
                    for (Packet packet : packets) {
                        byte[] serialized = NettyChannelHandler.this.packetEncoderRegistry.get(packet.getType()).encode(packet);
                        byte[] head = packet.getType().getBytes(StandardCharsets.UTF_8);
                        byte[] finalData = ArrayUtil.connect(new byte[]{(byte) head.length}, head, serialized);
                        ctx.writeAndFlush(BufferBuilder.wrap(finalData));
                    }
                    ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
                    Registery.getNetworkEventBus().callEvent(new ChannelTimingEvent(),this.sending);
                }, sendingspeed, TimeUnit.MILLISECONDS);
    }
}
