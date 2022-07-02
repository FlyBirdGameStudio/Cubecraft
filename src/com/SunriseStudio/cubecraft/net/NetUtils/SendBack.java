package com.SunriseStudio.cubecraft.net.NetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SendBack {
    public static byte[] compress(String str, String encoding) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes(encoding));
        gzip.close();
        return out.toByteArray();
    }

    public static String decompress(byte[] bytes) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }

}
