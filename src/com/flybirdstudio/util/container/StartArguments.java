package com.flybirdstudio.util.container;

import java.util.HashMap;

public class StartArguments {
    public HashMap<String,String> dispatchedArgs=new HashMap<>();

    public StartArguments(String[] args){
        for (String arg:args){
            StringBuilder sb=new StringBuilder();
            String k=null;
            String v=null;
            for (char c:arg.toCharArray()){
                if(c=='='){
                    k=sb.toString();
                    sb=new StringBuilder();
                }else{
                    sb.append(c);
                }
            }
            v=sb.toString();
            dispatchedArgs.put(k,v);
        }
    }


    public int getValueAsInt(String id, int fallback) {
        return Integer.parseInt(dispatchedArgs.getOrDefault(id, String.valueOf(fallback)));
    }

    public boolean getValueAsBoolean(String id,boolean fallback){
        return Boolean.parseBoolean(dispatchedArgs.getOrDefault(id, String.valueOf(fallback)));
    }

    public String getValueAsString(String id, String fallback) {
        return dispatchedArgs.getOrDefault(id,fallback);
    }
}
