package io.flybird.util;

import java.util.HashMap;

public class DebugInfoHandler {
    private final HashMap<String,String> map=new HashMap<>();
    public void put(String id,String info){
        map.put(id, info);
    }

    public String get(String id){
        return map.getOrDefault(id,"");
    }

    public void putI(String id, int i) {
        put(id,String.valueOf(i));
    }

    public int getI(String id){
        if(map.containsKey(id)){
            return Integer.parseInt(map.get(id));
        }else{
            return 0;
        }
    }

    public void putD(String s, double d) {
        put(s,String.valueOf(d));
    }

    public double getD(String id){
        if(map.containsKey(id)){
            return Double.parseDouble(map.get(id));
        }else{
            return Double.NaN;
        }
    }
}
