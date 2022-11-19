package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.NetWorkEventBus;
import io.flybird.cubecraft.net.handler.INetHandler;

public abstract class NettyPipeline {
    private NetWorkEventBus eventBus=new NetWorkEventBus();

    public void registerNetHandler(INetHandler handler){
        this.eventBus.registerEventListener(handler);
    }

    public void unregisterNetHandler(INetHandler handler){
        this.eventBus.unregisterEventListener(handler);
    }

    public abstract void init(String addr, int port);
}
