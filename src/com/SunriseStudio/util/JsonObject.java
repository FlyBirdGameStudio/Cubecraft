package com.sunrisestudio.util;

import com.google.gson.*;

public interface JsonObject<T> extends JsonDeserializer<T>,JsonSerializer<T> {
}
