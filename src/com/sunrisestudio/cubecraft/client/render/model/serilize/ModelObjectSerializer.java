package com.sunrisestudio.cubecraft.client.render.model.serilize;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sunrisestudio.cubecraft.client.render.model.object.ModelObject;

import java.lang.reflect.Type;

public class ModelObjectSerializer implements JsonDeserializer<ModelObject> {
    @Override
    public ModelObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
