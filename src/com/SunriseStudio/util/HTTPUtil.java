package com.sunrisestudio.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {
    private static final String USER_AGENT = "Mozilla/5.0";

    public static String get(String url){
        String str="";
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",USER_AGENT);

            InputStream in = con.getInputStream();
            str = new String(in.readAllBytes());
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }

    public static String post(String url){
        String str="";
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent",USER_AGENT);

            InputStream in = con.getInputStream();
            str = new String(in.readAllBytes());
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return str;
    }
}
