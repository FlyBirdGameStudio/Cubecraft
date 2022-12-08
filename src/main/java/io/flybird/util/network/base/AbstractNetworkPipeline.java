package io.flybird.util.network.base;

import io.flybird.util.event.CachedEventBus;
import io.flybird.util.network.NetWorkEventBus;
import io.flybird.util.network.handler.INetHandler;
import io.flybird.util.event.EventBus;
import io.flybird.util.event.EventListener;

public abstract class AbstractNetworkPipeline {
    protected final NetWorkEventBus packetEventBus =new NetWorkEventBus();
    protected final EventBus eventBus =new CachedEventBus();

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
