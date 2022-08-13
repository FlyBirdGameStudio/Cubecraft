package com.sunrisestudio.util.file.faml;

import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class FAMLLoader {
    private final HashMap<Type,FAMLDeserializer<?>> deserializers=new HashMap<>();
    private final HashMap<Type,FAMLSerializer<?>>serializers=new HashMap<>();

    public <T>T fromFAML(Document source, Type typeOfT){
        FAMLDeserializer<T> d;
        try {
             d = (FAMLDeserializer<T>) this.deserializers.get(typeOfT);
        }catch (ClassCastException e){
            throw new IllegalStateException("deserializer exception!");
        }
        return d.deserialize(source,this);
    }

    public <T>Document toFAML(T source, Type typeOfT){
        FAMLSerializer<T> d;
        try {
            d = (FAMLSerializer<T>) this.serializers.get(typeOfT);
        }catch (ClassCastException e){
            throw new IllegalStateException("deserializer exception!");
        }
        //todo:fix this
        d.serialize(source,null);
        return null;
    }

    public void registerSerializer(Type t,FAMLSerializer<?> serializer){
        this.serializers.put(t,serializer);
    }

    public void registerDeserializer(Type t,FAMLDeserializer<?> serializer){
        this.deserializers.put(t,serializer);
    }


    public static Document loadXML(String path){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document;
        try {
            document = db.parse(ResourceManager.instance.getResource(path,path));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
