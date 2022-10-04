package com.flybirdstudio.util.file.faml;

import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
