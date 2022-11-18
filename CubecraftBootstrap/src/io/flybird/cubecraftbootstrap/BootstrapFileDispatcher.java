package io.flybird.cubecraftbootstrap;

import com.google.gson.*;

import java.lang.reflect.Type;

public class BootstrapFileDispatcher implements JsonDeserializer<RunConfiguration[]> {
    @Override
    public RunConfiguration[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] lib=jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("libraries"),String[].class);
        JsonArray jsonArray=jsonElement.getAsJsonObject().get("instances").getAsJsonArray();
        RunConfiguration[] cfgs=new RunConfiguration[jsonArray.size()];
        for (int i=0;i<jsonArray.size();i++){
            Instance in=jsonDeserializationContext.deserialize(jsonArray.get(i),Instance.class);
            cfgs[i]=new RunConfiguration(in.name(),lib,in);
        }
        return cfgs;
    }
}
