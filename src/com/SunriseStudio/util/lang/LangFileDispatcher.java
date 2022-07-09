package com.sunrisestudio.util.lang;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
public class LangFileDispatcher {
    public static HashMap<String,String> getLangFromMap(InputStream inputStream){
        HashMap<String,String> result=new HashMap<>();
        byte[] b;
        try {
             b=inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] str=new String(b, StandardCharsets.UTF_8).split("\n");

        for (String arg:str){
            StringBuilder sb=new StringBuilder();
            String k=null;
            String v;
            for (char c:arg.toCharArray()){
                if(c=='='){
                    k=sb.toString();
                    sb=new StringBuilder();
                }else{
                    sb.append(c);
                }
            }
            v=sb.toString();
            result.put(k,v);
        }
        return result;
    }
}
