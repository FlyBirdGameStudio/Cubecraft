package com.SunriseStudio.cubecraft.net;

import com.SunriseStudio.cubecraft.net.handler.ChannelHandlerServer;
import com.SunriseStudio.cubecraft.net.handler.IServerHandler;
import com.SunriseStudio.cubecraft.util.ReflectHelper;
import com.SunriseStudio.cubecraft.util.collections.options.Option;
import com.SunriseStudio.cubecraft.util.collections.options.Options;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ServerIO {
    private final Option option;

    public ServerIO(Option option){
         this.option=option;
    }



    private final ArrayList<IServerHandler> handlers=new ArrayList<>();

    /**
     * search all class in jvm,find class
     */
    public void registerChannelHandlerServer(){
        try{
            this.handlers.clear();
            //get class with annotation
            List<Class<?>> result=new ArrayList<>();
            Iterator<Class<?>> it= ReflectHelper.getAllClass();
            while (it.hasNext()){
                Class<?> clazz=it.next();
                if(Arrays.stream(clazz.getAnnotations()).anyMatch(annotation -> annotation instanceof ChannelHandlerServer)){
                    this.handlers.add((IServerHandler) clazz.getConstructor().newInstance());
                }
            }
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            //fuck!
        }
    }

    public void initNettyPipeline() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(ServerIO.this.handlers.toArray(new ChannelHandler[0]));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind((Integer) option.get("port")).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new Exception("can not init netty pipeline:"+e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    static {
        Options.setDefault("server.port",25575);
    }
}
