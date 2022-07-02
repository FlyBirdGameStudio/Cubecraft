package com.SunriseStudio.cubecraft.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
	
	public static URL getURLSafely(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static String sendGet(String link) throws IOException {
		URL url = getURLSafely(link);
		if(url==null) {
			return null;
		}
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.connect();
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
		String line;
		while((line=in.readLine())!=null) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}
}
