package io.flybird.cubecraft.client.render.model;

import io.flybird.cubecraft.client.render.model.object.Model;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.cubecraft.register.Registry;

import java.util.HashMap;

public class ModelManager <I extends Model>{
    private final Class<I> clazz;

    public ModelManager(Class<I> clazz){
        this.clazz = clazz;
    }

    private final HashMap<String,I> models =new HashMap<>();

    public I get(String id) {
        return models.get(id);
    }

    public void load(String file){
        String json= ResourceManager.instance.getResource(file).getAsText();
        I model = Registry.getJsonReader().fromJson(json, clazz);
        this.models.put(file,model);
    }

    public void load(ResourceLocation loc){
        load(loc.format());
    }
}
