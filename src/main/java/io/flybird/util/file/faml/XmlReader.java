package io.flybird.util.file.faml;

import org.w3c.dom.Element;

import java.lang.reflect.Type;
import java.util.HashMap;

public class XmlReader {
    private final HashMap<Type,FAMLDeserializer<?>> deserializers=new HashMap<>();

    public <T>T deserialize(Element source, Type typeOfT){
        FAMLDeserializer<T> d;
        try {
             d = (FAMLDeserializer<T>) this.deserializers.get(typeOfT);
        }catch (ClassCastException e){
            throw new IllegalStateException("deserializer exception!");
        }
        return d.deserialize(source,this);
    }

    public void registerDeserializer(Type t,FAMLDeserializer<?> serializer){
        this.deserializers.put(t,serializer);
    }
}
