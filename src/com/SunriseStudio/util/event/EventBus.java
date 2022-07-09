package com.sunrisestudio.util.event;

import com.sunrisestudio.util.container.CollectionUtil;
import com.sunrisestudio.util.container.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class EventBus<A, L, I extends TypeGettableEventItem,I2 extends TypeGettableEventItem> {
    private final HashMap<String, ArrayList<Pair<L, Method>>> methods = new HashMap<>();

    public void callEvent(I2 event) {
        for (Pair<L, Method> pair : this.methods.get(event.getType())) {
            try {
                pair.t2().invoke(pair.t1(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public EventBus(HashMap<String, Class<? extends L>> listenerClasses, AnnotationPredicateCallBack<A> callBack) {
        CollectionUtil.iterateMap(listenerClasses, (key, item) -> {
            try {
                L listener = item.getDeclaredConstructor().newInstance();
                for (Method m : item.getMethods()) {
                    AtomicReference<A> an = new AtomicReference<>();
                    if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> callBack.instanceOf(annotation))) {
                        this.methods.computeIfAbsent(callBack.getType(an.get()), k -> new ArrayList<>());
                        this.methods.get(callBack.getType(an.get())).add(new Pair<>(listener, m));
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
