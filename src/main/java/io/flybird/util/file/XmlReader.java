package io.flybird.util.file;

import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * simple Gson liked xml reader
 *
 * @author GrassBlock2022
 */
public class XmlReader {
    private final HashMap<Type,FAMLDeserializer<?>> deserializers=new HashMap<>();

    /**
     * select a right deserializer,and deserialize
     * @param source src
     * @param typeOfT type
     * @return deserialized object
     * @param <T> Template class
     */
    public <T>T deserialize(Element source, Type typeOfT){
        FAMLDeserializer<T> d;
        try {
             d = (FAMLDeserializer<T>) this.deserializers.get(typeOfT);
        }catch (ClassCastException e){
            throw new IllegalStateException("deserializer exception!");
        }
        return d.deserialize(source,this);
    }

    /**
     * register deserializer
     * @param t type
     * @param serializer deserializer
     */
    public void registerDeserializer(Type t,FAMLDeserializer<?> serializer){
        this.deserializers.put(t,serializer);
    }
}
