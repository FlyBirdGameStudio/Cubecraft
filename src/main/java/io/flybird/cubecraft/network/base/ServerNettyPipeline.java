package io.flybird.cubecraft.network.base;

import io.flybird.cubecraft.network.packet.PacketEventHandler;
import io.flybird.cubecraft.network.event.ChannelInitializeEvent;
import io.flybird.util.logging.LogHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.*;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.HashMap;


/**
 * <h3>a packaged server netty pipeline for tcp.</h3>
 * <li>call a channel init event when netty try to init a channel.</li>
 * <li>you could directly touch netty using listened {@link ChannelInitializeEvent}.</li>
 * <li>or using general {@link PacketEventHandler} to handle event.</li>
 */
public class ServerNettyPipeline extends AbstractNetworkPipeline {
    public static final int DEFAULT_SERVER_PORT = 11451;

    private final LogHandler logHandler = LogHandler.create("Server/Network");
    private final HashMap<InetSocketAddress, SocketChannel> channelMapping = new HashMap<>();
    private final HashMap<InetSocketAddress, NettyChannelHandler> handlerMapping = new HashMap<>();
    public int port = DEFAULT_SERVER_PORT;

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void init(int thread) {
        new Thread(() -> {
            logHandler.info("starting server on %s", this.port);
            //创建两个线程组 boosGroup、workerGroup
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)//keep channel alive :D
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(@NotNull SocketChannel ch) {
                                eventBus.callEvent(new ChannelInitializeEvent(ServerNettyPipeline.this, ch));
                                NettyChannelHandler handler = new NettyChannelHandler(packetEventBus);
                                //给pipeline管道设置处理器
                                ch.pipeline().addLast(handler);
                                channelMapping.put(ch.remoteAddress(), ch);
                                handlerMapping.put(ch.remoteAddress(), handler);
                            }
                        });
                ChannelFuture channelFuture = bootstrap.bind(this.port).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                this.logHandler.exception(e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                this.logHandler.info("server io stopped...");
            }
        }, "server_channel_initializer").start();
    }

    public NettyChannelHandler getDefaultHandler(InetSocketAddress addr) {
        return this.handlerMapping.get(addr);
    }

    public SocketChannel getChannel(InetSocketAddress addr) {
        return this.channelMapping.get(addr);
    }
}
