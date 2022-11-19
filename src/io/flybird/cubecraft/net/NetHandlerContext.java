package io.flybird.cubecraft.net;

import io.flybird.cubecraft.net.base.NettyChannelHandler;

import java.net.InetSocketAddress;

public record NetHandlerContext(NettyChannelHandler handler, InetSocketAddress from, InetSocketAddress host) {
}
