package io.flybird.util.file;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void createFileRelative(String rel){
        File f=new File(System.getProperty("user.dir")+rel);
        if(!f.getParentFile().exists()){
            f.mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
