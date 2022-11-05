package io.flybird.cubecraftbootstrap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class InstanceDispatcher implements JsonDeserializer<Instance> {
    @Override
    public Instance deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] mods=jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("mods"),String[].class);
        String core=jsonElement.getAsJsonObject().get("core").getAsString();
        String name=jsonElement.getAsJsonObject().get("name").getAsString();
        String[] vm=jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("vm"),String[].class);
        String[] arg=jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("args"),String[].class);

        return new Instance(name,core,mods,arg,vm);
    }
}
