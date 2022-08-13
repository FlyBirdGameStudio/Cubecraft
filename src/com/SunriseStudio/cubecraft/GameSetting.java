package com.sunrisestudio.cubecraft;

import com.sunrisestudio.util.container.CollectionUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class GameSetting {
    private File file;
    public HashMap<String,Object> buffer=new HashMap<>();
    public Properties properties=new Properties();

    public GameSetting(String path){
        this.file=new File(path);
        if(!this.file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {}
        }
    }

    public Object getValue(String path,Object _default){
        return this.properties.getOrDefault(path,_default);
    }

    public int getValueAsInt(String path, int i){
        return Integer.valueOf((String) this.properties.getOrDefault(path,String.valueOf(i)));
    }



    public void setValue(String path,Object data){
        buffer.put(path, data);
    }

    public void setValueNoBuffer(String path,Object data){
        this.properties.put(path,data);
    }

    public void flush(){
        CollectionUtil.iterateMap(this.buffer, (key, item) -> properties.put(key,item));
        this.buffer.clear();
    }

    public void read(){
        InputStream in;
        try {
            in = new FileInputStream(file);
            this.properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            this.properties.store(out,"mckuhei你不得好死");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameSetting instance=new GameSetting(Start.getGamePath()+"/data/configs/settings.properties");

    public int FXAA=1;
}
