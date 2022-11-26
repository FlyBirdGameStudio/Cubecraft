package io.flybird.cubecraft.net.event;

import io.flybird.cubecraft.net.base.NettyPipeline;
import io.flybird.util.event.Event;
import io.netty.channel.Channel;

public record ChannelInitializeEvent(NettyPipeline pipeline, Channel ch) implements Event {
}
