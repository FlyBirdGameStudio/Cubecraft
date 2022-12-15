package io.flybird.cubecraft.client.resources;

import io.flybird.cubecraft.client.ClientMain;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.event.ClientResourceReloadEvent;
import io.flybird.util.event.DirectEventBus;

import io.flybird.util.event.EventBus;
import io.flybird.util.logging.LogHandler;
import io.flybird.util.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceManager {
    public static final ResourceManager instance=new ResourceManager();
    public final ArrayList<ResourcePack> resourcePacks=new ArrayList<>();
    public final LogHandler logHandler=LogHandler.create("Client/ResourceLoader");
    private final EventBus eventBus=new DirectEventBus();
    private final ArrayList<String> namespaces=new ArrayList<>();

    public void reload(Cubecraft client) {
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.DETECT);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.BLOCK_MODEL);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.BLOCK_TEXTURE);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.COLOR_MAP);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.FONT_TEXTURE);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.LANGUAGE);
        this.reloadStage(new ClientResourceReloadEvent(client,this),ResourceLoadStage.UI_CONTROLLER);
    }

    public Resource getResource(String path){
        InputStream inputStream=null;
        for (ResourcePack resourcePack:resourcePacks){
            try {
                inputStream=resourcePack.getInput(path);
            } catch (IOException e) {
                logHandler.warning("can not read file:"+path+",because of "+e);
            }
            if(inputStream!=null){
                break;
            }
        }
        if (inputStream==null){
            inputStream=this.getClass().getResourceAsStream(path);
        }
        if(inputStream==null){
            throw new RuntimeException("could not load anyway:"+path);
        }
        return new Resource(path,inputStream);
    }

    public Resource getResource(ResourceLocation resourceLocation){
        return getResource(resourceLocation.format());
    }

    public static void createResourceFolder(){
        new File(ClientMain.getGamePath()+"/data/resource_packs").mkdirs();
        new File(ClientMain.getGamePath()+"/data/shader_packs").mkdirs();
        new File(ClientMain.getGamePath()+"/data/plugins").mkdirs();
        new File(ClientMain.getGamePath()+"/data/mods").mkdirs();
        new File(ClientMain.getGamePath()+"/data/logs").mkdirs();
        new File(ClientMain.getGamePath()+"/data/saves").mkdirs();
        new File(ClientMain.getGamePath()+"/data/configs").mkdirs();
        new File(ClientMain.getGamePath()+"/data/cache").mkdirs();
    }

    protected final ArrayList<EventListener> listeners = new ArrayList<>();
    public void registerEventListener(EventListener listener) {
        this.eventBus.registerEventListener(listener);
        this.listeners.add(listener);
    }

    public void reloadStage(ClientResourceReloadEvent e,ResourceLoadStage stage) {
        for (EventListener el : this.listeners) {
            Method[] ms = el.getClass().getMethods();
            for (Method m : ms) {
                if (Arrays.stream(m.getAnnotations()).anyMatch(annotation -> annotation instanceof ResourceLoadHandler)) {
                    ResourceLoadHandler a=m.getAnnotation(ResourceLoadHandler.class);
                    if (m.getParameters()[0].getType() == e.getClass() && a.stage() == stage) {
                        try {
                            m.invoke(el, e);
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            throw new RuntimeException(e2);
                        }
                    }
                }
            }
        }
    }

    public void addNameSpace(String space) {
        if(!this.namespaces.contains(space)){
            this.namespaces.add(space);
        }
    }

    public List<String> getNameSpaces() {
        return this.namespaces;
    }
}
