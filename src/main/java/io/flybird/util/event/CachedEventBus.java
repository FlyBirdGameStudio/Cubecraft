package io.flybird.util.event;

import io.flybird.util.container.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * use a cache for method and listener to advance speed.<br/>
 * - takes more mem use for higher speed.<br/>
 * - recommend for more listeners,or strict delay<br/>
 */
public class CachedEventBus implements EventBus {
    //event type/[handler/handle method]
    private final HashMap<Class<Event>, HashMap<String,Pair<Object, Method>>> map=new HashMap<>();

    @Override
    public void callEvent(Event event, Object... param) {
        HashMap<String,Pair<Object, Method>> list=this.map.get(event.getClass());
        if(list!=null){
            for (Pair<Object, Method> node:list.values()){
                try {
                    if(param.length==0) {
                        node.t2().invoke(node.t1(), event);
                    }else {
                        node.t2().invoke(node.t1(), event,param);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void registerEventListener(EventListener el) {
        Method[] ms = el.getClass().getMethods();
        for (Method m : ms) {
            if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                Class<Event> type= (Class<Event>) m.getParameterTypes()[0];
                this.map.computeIfAbsent(type, k -> new HashMap<>(128));
                this.map.get(type).put(el.getClass().getName()+"/"+m.getName(),new Pair<>(el,m));
            }
        }
    }

    @Override
    public void unregisterEventListener(EventListener el) {
        Method[] ms = el.getClass().getMethods();
        for (Method m : ms) {
            if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof EventHandler)) {
                Class<Event> type= (Class<Event>) m.getParameterTypes()[0];
                this.map.get(type).remove(el.getClass().getName()+"/"+m.getName());
            }
        }
    }
}
