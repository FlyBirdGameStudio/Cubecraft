package io.flybird.util.file;

import org.w3c.dom.Element;

/**
 * simple deserializer
 * @param <T> Template class
 */
public interface FAMLDeserializer<T> {
    T deserialize(Element element, XmlReader famlLoadingContext);
}
