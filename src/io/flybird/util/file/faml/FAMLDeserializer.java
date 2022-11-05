package io.flybird.util.file.faml;

import org.w3c.dom.Element;

public interface FAMLDeserializer<T> {
    T deserialize(Element element, XmlReader famlLoadingContext);
}
