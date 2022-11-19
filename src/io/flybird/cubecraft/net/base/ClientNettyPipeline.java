package io.flybird.cubecraft.net.base;

import io.flybird.util.LogHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * this pipeline hold a netty pipeline in client side
 * <li>use netty task to drive network system.</li>
 * <li>remember:we can not make sure network thread is working at thread Client_main</li>
 * <li>do not try to do any thread-locked operation(such as that annoying openGL)</li>
 */
public class ClientNettyPipeline extends NettyPipeline {
    boolean running=false;
    private NioEventLoopGroup eventExecutors;
    private final LogHandler logHandler = LogHandler.create("ClientNetworkInitializer", "game");
    private InetSocketAddress server, host;


    public void setServerAddress(InetSocketAddress server) {
        this.server = server;
    }

    public void setHostAddress(InetSocketAddress host) {
        this.host = host;
    }

    /**
     * init netty channel,set netHandler
     */
    @Override
    public void init(String remote,int port) {
        this.host = new InetSocketAddress(0);
        running=true;
        new Thread(() -> {
            this.logHandler.info("connecting to " + remote + ":" + port);
            this.eventExecutors = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        //设置客户端的通道实现类型
                        .channel(NioSocketChannel.class)
                        //使用匿名内部类初始化通道
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                //添加客户端通道的处理器
                                ch.pipeline().addLast("cubecraft:default", new NettyChannelHandler(server, host,null));
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect(this.server).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                this.logHandler.error("channel listen interrupt:" + e);
            } finally {
                this.shutdown();
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
}
