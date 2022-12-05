package io.flybird.util.network.event;

import io.flybird.util.network.base.AbstractNetworkPipeline;
import io.flybird.util.event.Event;
import io.netty.channel.Channel;

public record ChannelInitializeEvent(AbstractNetworkPipeline pipeline, Channel ch) implements Event {
}
