package io.flybird.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * use direct reflect access to listeners.<br/>
 * - takes less speed for lower mem usage<br/>
 * - recommend for fewer listeners,or not-strict delay<br/>
 */
public class DirectEventBus implements EventBus {
    protected final ArrayList<EventListener> listeners = new ArrayList<>();

    public void callEvent(Event event,Object... param) {
        for (EventListener el : ((ArrayList<EventListener>) this.listeners.clone())) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                    boolean equals=true;
                    for (int i=0;i< param.length;i++){
                        if(m.getParameters()[i+1].getType()!=param[i]){
                            equals=false;
                        }
                    }

                    if (m.getParameters()[0].getType() == event.getClass()&&equals) {
                        try {
                            m.invoke(el, event,param);
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
