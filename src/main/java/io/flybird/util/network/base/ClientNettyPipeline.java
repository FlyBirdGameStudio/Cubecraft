package io.flybird.util.network.base;

import io.flybird.util.network.event.ChannelInitializeEvent;
import io.flybird.util.logging.LogHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * this pipeline hold a netty pipeline in client side
 * <li>use netty task to drive network system.</li>
 * <li>remember:we can not make sure network thread is working at thread Client_main</li>
 * <li>do not try to do any thread-locked operation(such as that annoying openGL)</li>
 */
public class ClientNettyPipeline extends AbstractNetworkPipeline {
    boolean running=false;
    private NioEventLoopGroup eventExecutors;
    private final LogHandler logHandler = LogHandler.create("Client/Network");
    private InetSocketAddress server, local;
    protected final NettyChannelHandler handler=new NettyChannelHandler(this.packetEventBus);

    public void setServerAddr(InetSocketAddress server) {
        this.server = server;
    }

    /**
     * init netty channel,set netHandler
     */
    @Override
    public void init(int threads) {
        this.local = new InetSocketAddress(0);
        running=true;
        new Thread(() -> {
            this.logHandler.info("connecting to " + server.getAddress() + ":" + server.getPort());
            this.handler.setLocalAddress(this.local);
            this.eventExecutors = new NioEventLoopGroup(threads);
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        //设置客户端的通道实现类型
                        .channel(NioSocketChannel.class)
                        //使用匿名内部类初始化通道
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(@NotNull SocketChannel ch) {
                                eventBus.callEvent(new ChannelInitializeEvent(ClientNettyPipeline.this,ch));
                                ch.pipeline().addLast(ClientNettyPipeline.this.handler);
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(this.server).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                logHandler.exception(e);
            } finally {
                logHandler.info("client io stopped...");
                eventExecutors.shutdownGracefully();
            }
        }, "channel_initializer_client").start();
    }

    /**
     * stop netty channel,gracefully stop everything
     */
    public void shutdown() {
        this.logHandler.info("shutting down client io...");
        this.eventExecutors.shutdownGracefully();
        this.logHandler.info("client io stopped");
        running=false;
    }

    public boolean isRunning() {
        return running;
    }

    public NettyChannelHandler getHandler() {
        return handler;
    }
}
