package com.sunrisestudio.cubecraft.net;

import com.sunrisestudio.util.LogHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.*;

public class ClientIO {
    private final ArrayList<ClientChannelHandler> handlers=new ArrayList<>();
    private NioEventLoopGroup eventExecutors;
    private final LogHandler logHandler=LogHandler.create("clientIO","client");

    /**
     * init netty channel,set netHandler
     * @param initialHost initial host ip
     * @param port initial port
     */
    public void init(String initialHost, int port){
        this.logHandler.info("starting client io at "+initialHost+":"+port);
        this.eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(ClientIO.this.handlers.toArray(new ChannelHandler[0]));
                        }
                    });
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect(initialHost, port).sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            this.logHandler.error("channel listen interrupt:"+e);
        } finally {
            this.shutdown();
        }
    }

    /**
     * stop netty channel
     */
    public void shutdown(){
        this.logHandler.info("shutting down client io...");
        this.eventExecutors.shutdownGracefully();
        this.logHandler.info("client io stopped");
    }
}
