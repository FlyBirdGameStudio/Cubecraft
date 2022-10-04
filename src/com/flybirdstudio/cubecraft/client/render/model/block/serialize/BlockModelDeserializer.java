package com.flybirdstudio.cubecraft.client.render.model.block.serialize;

import com.flybirdstudio.cubecraft.client.render.model.block.BlockModel;
import com.flybirdstudio.cubecraft.client.render.model.block.IBlockModelComponent;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BlockModelDeserializer implements JsonDeserializer<BlockModel> {
    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray comp=jsonElement.getAsJsonArray();
        ArrayList<IBlockModelComponent> component=new ArrayList<>();
        for (int i = 0; i < comp.size(); i++) {
            jsonDeserializationContext.deserialize(comp.get(i),IBlockModelComponent.class);
        }
        return new BlockModel("id", "namespace", component);
    }
}
