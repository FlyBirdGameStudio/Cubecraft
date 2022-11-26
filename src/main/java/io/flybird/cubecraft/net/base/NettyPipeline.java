package io.flybird.cubecraft.net.base;

import io.flybird.cubecraft.net.NetWorkEventBus;
import io.flybird.cubecraft.net.handler.INetHandler;
import io.flybird.util.event.EventBus;
import io.flybird.util.event.EventListener;

public abstract class NettyPipeline {
    protected final NetWorkEventBus packetEventBus =new NetWorkEventBus();
    protected final EventBus eventBus =new EventBus();

    public void registerNetHandler(INetHandler handler){
        this.packetEventBus.registerEventListener(handler);
    }

    public void unregisterNetHandler(INetHandler handler){
        this.packetEventBus.unregisterEventListener(handler);
    }

    public void registerNetHandler(EventListener listener){
        this.eventBus.registerEventListener(listener);
    }

    public void unregisterNetHandler(EventListener listener){
        this.eventBus.unregisterEventListener(listener);
    }

    public NetWorkEventBus getPacketEventBus() {
        return packetEventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public abstract void init(int thread);
}
