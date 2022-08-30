package com.flybirdstudio.util.container.namespace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * pack of {@link NameSpaceMap}
 *
 * @param <I> item
 * @param <D> item depending on object class
 */
public class NameSpacedRegisterMap<I, D> extends NameSpaceMap<I>{
    private final NameSpacedRegisterMap<D, ?> depend;

    public NameSpacedRegisterMap(NameSpacedRegisterMap<D, ?> map) {
        super(":");
        this.depend = map;
    }

    /**
     * namespace item:put
     * if there's no namespace it will create a new one.
     *
     * @param id        id
     * @param namespace namespace
     * @param item      item
     */
    public void registerItem(String id, String namespace, I item) {
        this.set(id, namespace, item);
    }

    /**
     * use all string as alternative method. for example:
     * "sunrise_studio:jsysb"will do this:
     * namespace=sunrise_studio,id=jsysb
     *
     * @param all  string
     * @param item item
     */
    public void registerItem(String all, I item) {
        registerItem(all.split(":")[1], all.split(":")[0], item);
    }


    /**
     * put a class of {@link I}
     *
     * @param id        id
     * @param namespace namespace
     * @param item      item
     */
    public void registerClass(String id, String namespace, Class<? extends I> item) {
        try {
            this.set(id, namespace, item.getConstructor().newInstance());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * put a class of {@link I}
     *
     * @param all  full name(jsysb:jsysb)
     * @param item item
     */
    public void registerClass(String all, Class<? extends I> item) {
        registerClass(all.split(":")[1], all.split(":")[0], item);
    }


    /**
     * put a class of getter of{@link I}
     * @param clazz class
     */
    public void registerGetter(Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            NameSpaceItemGetter getter = m.getAnnotation(NameSpaceItemGetter.class);
            if (getter != null) {
                GetterDepend dep=m.getAnnotation(GetterDepend.class);
                try {
                    if (dep!=null) {
                        D depend = this.depend.get(dep.id(),dep.namespace());
                        this.set(getter.id(), getter.namespace(), (I) m.invoke(clazz.getConstructor().newInstance(),depend));
                    }else {
                        this.set(getter.id(), getter.namespace(), (I) m.invoke(clazz.getConstructor().newInstance()));
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
