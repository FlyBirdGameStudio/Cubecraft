package com.flybirdstudio.cubecraft.net;

import com.flybirdstudio.util.container.ArrayQueue;
import com.flybirdstudio.util.event.Event;
import com.flybirdstudio.util.event.EventBus;
import com.flybirdstudio.util.event.EventHandler;
import com.flybirdstudio.util.event.EventListener;
import com.flybirdstudio.util.net.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class NetWorkEventBus{
    private ArrayList<EventListener> listeners;

    public void callEvent(Event event, ArrayQueue<Packet> sendQueue) {
        for (EventListener el : this.listeners) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                    if (m.getParameterCount() == 2 && m.getParameters()[1].getType() == event.getClass()) {
                        try {
                            m.invoke(el, event,sendQueue);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public void registerEventListener(EventListener el){
        if(this.listeners.contains(el)) {
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
