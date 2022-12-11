package io.flybird.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * use direct reflect access to listeners.<br/>
 * - takes less speed for lower mem usage<br/>
 * - recommend for fewer listeners,or not-strict delay<br/>
 *
 * @author GrassBlock2022
 */
public class DirectEventBus implements EventBus {
    protected final ArrayList<EventListener> listeners = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    public void callEvent(Event event) {
        for (EventListener el : ((ArrayList<EventListener>) this.listeners.clone())) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                    try {
                        m.invoke(el, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void registerEventListener(EventListener el) {
        this.listeners.add(el);
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterEventListener(EventListener el) {
        this.listeners.remove(el);
    }
}
