package io.flybird.util.file.faml;

import org.w3c.dom.Element;

public interface FAMLSerializer<T> {
    void serialize(T obj, Element element);
}
