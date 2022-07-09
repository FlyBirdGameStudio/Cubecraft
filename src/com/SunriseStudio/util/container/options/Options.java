package com.sunrisestudio.util.container.options;

import java.util.HashMap;
import java.util.Set;

public class Options {
    private static final HashMap<String,Object> defaults=new HashMap<>();

    public static void setDefault(String path,Object value){
        defaults.put(path,value);
    }

    public static Object getDefault(String path){
        return defaults.get(path);
    }

    public static HashMap<String,Object> getValues(String nameSpace){
        HashMap<String, Object> result=new HashMap<>();
        Set<String> keys=defaults.keySet();
        for (String k:keys){
            if(k.startsWith(nameSpace)&&k.charAt(nameSpace.length())=='.'){
                result.put(k,defaults.get(k));
            }
        }
        return result;
    }
}
