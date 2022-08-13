package com.sunrisestudio.cubecraft.client.render.model;

import com.sunrisestudio.cubecraft.client.render.model.object.Model;
import com.sunrisestudio.cubecraft.client.resources.ResourceManager;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.util.container.namespace.NameSpaceMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModelManager <I extends Model>{
    private Class<I> clazz;
    private String fallback;

    public ModelManager(String fallbackPath,Class<I> clazz){
        fallback=fallbackPath;
    }

    private final NameSpaceMap<I> models =new NameSpaceMap<>(":");

    public I get(String id) {
        return models.get(id);
    }

    public I get(String id,String namespace) {
        return models.get(id,namespace);
    }

    public void load(String file){
        String json= null;
        try {
            json = new String(ResourceManager.instance.getResource(file,fallback).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        I model = Registry.getJsonReader().fromJson(json, clazz);
        this.models.set(model.getID(),model.getNameSpace(),model);
    }
}
