package io.flybird.util.container.namespace;

import io.flybird.cubecraft.internal.net.packet.PacketRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NameSpacedConstructingMap<I> {
    private final NameSpacedRegisterMap<Class<? extends I>,?> map=new NameSpacedRegisterMap<>(null);
    private final Class<?>[] params;

    public NameSpacedConstructingMap(Class<?>... params) {
        this.params = params;
    }

    /**
     * namespace item:put
     * if there's no namespace it will create a new one.
     *
     * @param id        id
     * @param namespace namespace
     * @param item      item
     */
    public void registerItem(String id, String namespace, Class<? extends I> item) {
        map.registerItem(id,namespace,item);
    }

    /**
     * use all string as alternative method. for example:
     * "sunrise_studio:jsysb"will do this:
     * namespace=sunrise_studio,id=jsysb
     *
     * @param all  string
     * @param item item
     */
    public void registerItem(String all, Class<? extends I> item) {
        registerItem(all.split(":")[1], all.split(":")[0], item);
    }

    /**
     * create a new instance from id.
     * @param all id
     * @param initArgs init args
     * @return object
     */
    public I create(String all,Object... initArgs){
        try {
            return map.get(all).getDeclaredConstructor(params).newInstance(initArgs);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * create a new instance from id.
     * @param namespace,id id
     * @param initArgs init args
     * @return object
     */
    public I create(String id,String namespace,Object... initArgs){
        try {
            return map.get(id, namespace).getDeclaredConstructor(params).newInstance(initArgs);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,I> createAll(Object... initArgs){
        Map<String,I> map=new HashMap<>();
        for (String all:this.map.idList()){
            map.put(all,create(all,initArgs));
        }
        return map;
    }

    public void registerItem(Class<? extends I> item) {
        TypeItem a=item.getDeclaredAnnotation(TypeItem.class);
        if(a==null){
            throw new RuntimeException("item does not contains TypeItem annotation,so can`t auto reg.");
        }
        registerItem(a.value(),item);
    }

    public void registerGetFunctionProvider(Class<?> clazz) {
        try {
            for (Method m : clazz.getMethods()) {
                ItemRegisterFunc getter = m.getAnnotation(ItemRegisterFunc.class);
                if (getter != null) {
                    if (m.getParameters().length == 1 && m.getParameters()[0].getType() == NameSpacedConstructingMap.class) {
                        m.invoke(clazz.getConstructor().newInstance(), this);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
