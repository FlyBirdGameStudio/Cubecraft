package com.flybirdstudio.util.file.faml;

import org.w3c.dom.Document;

public interface FAMLSerializer<T> {
    void serialize(T obj,Document doc);
}
