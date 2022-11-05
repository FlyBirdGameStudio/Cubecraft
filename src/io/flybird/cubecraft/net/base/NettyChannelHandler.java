package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.ChannelTimingEvent;
import io.flybird.cubecraft.net.INetHandler;
import io.flybird.cubecraft.register.Registry;
import io.flybird.util.container.ArrayQueue;
import io.flybird.util.container.ArrayUtil;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;
import io.flybird.util.event.EventListener;
import io.flybird.util.net.Packet;
import io.flybird.util.net.PacketDecoder;
import io.flybird.util.net.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NettyChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
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
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet){
        byte[] data = packet.content().array();
        byte headSize = data[0];
        String type = new String(ArrayUtil.copySub(1, headSize, data), StandardCharsets.UTF_8);
        Packet pkt = this.packetDecoderRegistry.get(type).decode(data);
        Registry.getNetworkEventBus().callEvent(pkt,sending);
        for (EventListener netHandler: Registry.getNetworkEventBus().getHandlers()){
            ((INetHandler) netHandler).setSendQueue(this.sending);
        }
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
                        ctx.writeAndFlush(BufferUtil.wrap(finalData));
                    }
                    ctx.pipeline().fireUserEventTriggered(new ChannelTimingEvent());
                    Registry.getNetworkEventBus().callEvent(new ChannelTimingEvent(),this.sending);
                }, sendingspeed, TimeUnit.MILLISECONDS);
    }
}
