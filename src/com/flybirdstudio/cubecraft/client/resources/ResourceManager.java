package com.flybirdstudio.cubecraft.client.resources;

import com.flybirdstudio.cubecraft.Start;
import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.FontRenderer;
import com.flybirdstudio.cubecraft.registery.Registery;
import com.flybirdstudio.starfish3d.render.textures.Texture2DTileMap;
import com.flybirdstudio.starfish3d.render.textures.TextureManager;
import com.flybirdstudio.starfish3d.render.textures.TextureStateManager;
import com.flybirdstudio.util.LogHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceManager {
    public static ResourceManager instance=new ResourceManager();
    public ArrayList<ResourcePack> resourcePacks=new ArrayList<>();
    public LogHandler logHandler=LogHandler.create("resouceLoader","client");

    public void reload(Cubecraft client) {
        this.logHandler.checkGLError("pre_font_load");
        FontRenderer.loadTextures(client);
        this.logHandler.checkGLError("post_font_load");

        File f[]=new File(Start.getGamePath()+"/resources/resource/textures/block").listFiles();
        String[] name=new String[f.length];
        int i=0;
        for (File f2:f){
            name[i]="/resource/textures/block/"+f2.getName();
            i++;
        }
        Texture2DTileMap terrain=Registery.getTextureManager().createTexture2DTileMap("cubecraft:terrain",false,true,16,name);
        TextureStateManager.setTextureMipMap(terrain,true);
    }


    public InputStream getResource(String path,String fallback){
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
        if (inputStream==null){
            logHandler.warning("can not read path,use fallback:"+fallback);
            inputStream=this.getClass().getResourceAsStream(fallback);
        }
        return inputStream;
    }


    public BufferedImage getImage(String path){
        try {
            return ImageIO.read(this.getResource(path, "resource/fallback/texture.png"));
        } catch (Exception e) {
            logHandler.warning("can not read textures:"+path+",because of "+e);
        }
        return new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
    }

    public static void createResourceFolder(){
        new File(Start.getGamePath()+"/data/resourcepacks").mkdirs();
        new File(Start.getGamePath()+"/data/shaderpacks").mkdirs();
        new File(Start.getGamePath()+"/data/plugins").mkdirs();
        new File(Start.getGamePath()+"/data/mods").mkdirs();
        new File(Start.getGamePath()+"/data/logs").mkdirs();
        new File(Start.getGamePath()+"/data/saves").mkdirs();
        new File(Start.getGamePath()+"/data/configs").mkdirs();
        new File(Start.getGamePath()+"/data/cache").mkdirs();
    }
}
