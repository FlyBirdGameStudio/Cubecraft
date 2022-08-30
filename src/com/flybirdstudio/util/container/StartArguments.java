package com.flybirdstudio.util.container;

import java.util.HashMap;

public class StartArguments {
    public HashMap<String,Object> dispatchedArgs=new HashMap<>();

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

    public Object getValue(String id, Object ifNull) {
        return dispatchedArgs.getOrDefault(id,ifNull);
    }
}
