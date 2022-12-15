package io.flybird.cubecraft.network;

import io.flybird.util.event.Event;
import io.flybird.util.event.EventListener;
import io.flybird.cubecraft.network.packet.PacketEventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class NetWorkEventBus{
    private final ArrayList<EventListener> listeners=new ArrayList<>();

    public void callEvent(Event event,NetHandlerContext context) {
        for (EventListener el : this.listeners) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof PacketEventHandler)) {
                    if (m.getParameterCount() == 2 &&
                            m.getParameters()[0].getType() == event.getClass()&&
                            m.getParameters()[1].getType() == NetHandlerContext.class) {
                        try {
                            m.invoke(el, event,context);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public void registerEventListener(EventListener el){
        if(!this.listeners.contains(el)) {
            this.listeners.add(el);
        }
    }

    public void unregisterEventListener(EventListener el){
        this.listeners.remove(el);
    }

    public ArrayList<EventListener> getHandlers() {
        return this.listeners;
    }
}
