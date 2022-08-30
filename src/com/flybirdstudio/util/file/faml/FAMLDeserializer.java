package com.flybirdstudio.util.file.faml;

import org.w3c.dom.Document;

public interface FAMLDeserializer<T> {
    T deserialize(Document d,FAMLLoader famlLoadingContext);
}
