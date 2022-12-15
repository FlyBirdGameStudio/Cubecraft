package io.flybird.cubecraft.client.render.model;

import io.flybird.cubecraft.client.render.model.object.Model;
import io.flybird.cubecraft.client.resources.Resource;
import io.flybird.cubecraft.client.resources.ResourceLocation;
import io.flybird.cubecraft.client.resources.ResourceManager;
import io.flybird.cubecraft.register.Registries;

import java.util.HashMap;

public class ModelManager <I extends Model>{
    private final Class<I> clazz;
    private final ResourceLocation fallback;

    public ModelManager(Class<I> clazz,ResourceLocation fallback){
        this.clazz = clazz;
        this.fallback=fallback;
    }

    private final HashMap<String,I> models =new HashMap<>();

    public I get(String id) {
        return models.get(id);
    }

    public void load(String file){
        Resource res;
        try{
            res=ResourceManager.instance.getResource(file);
        }catch (Exception e){
            res=ResourceManager.instance.getResource(fallback);
        }

        String json= res.getAsText();
        I model = Registries.createJsonReader().fromJson(json, clazz);
        this.models.put(file,model);
    }

    public void load(ResourceLocation loc){
        load(loc.format());
    }
}
