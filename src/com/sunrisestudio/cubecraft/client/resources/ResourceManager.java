package com.sunrisestudio.cubecraft.client.resources;

import com.sunrisestudio.cubecraft.Start;
import com.sunrisestudio.util.LogHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourceManager {
    public static ResourceManager instance=new ResourceManager();
    public ArrayList<ResourcePack> resourcePacks=new ArrayList<>();
    public LogHandler logHandler=LogHandler.create("resouceLoader","client");


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
            return ImageIO.read(this.getResource(path, "resources/resource/minecraft/fallback/texture.png"));
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
    }
}
