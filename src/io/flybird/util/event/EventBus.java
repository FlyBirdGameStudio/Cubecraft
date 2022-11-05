package io.flybird.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventBus {
    protected final ArrayList<EventListener> listeners = new ArrayList<>();

    private final ArrayList<Class<?>> params=new ArrayList<>();

    public EventBus (Class<?>... params){
        this.params.addAll(List.of(params));
    }

    public void callEvent(Event event,Object... param) {
        for (EventListener el : this.listeners) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                    boolean equals=true;
                    for (int i=0;i< params.size();i++){
                        if(m.getParameters()[i+1].getType()!=params.get(i)){
                            equals=false;
                        }
                    }

                    if (m.getParameters()[0].getType() == event.getClass()&&equals) {
                        try {
                            m.invoke(el, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public void registerEventListener(EventListener el){
        this.listeners.add(el);
    }

    public void unregisterEventListener(EventListener el){
        this.listeners.remove(el);
    }

    public ArrayList<EventListener> getHandlers() {
        return this.listeners;
    }
}
