package io.flybird.cubecraftbootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JarLoaderScanner {
    public final String root;

    public JarLoaderScanner(String root) {
        this.root = root;
    }

    public List<String> scanMod(){
        List<String> result=new ArrayList();
        File[] f =new File(this.root+"/data/mods").listFiles();
        for (File f2:f){
            result.add(f2.getAbsolutePath());
        }
        return result;
    }

    public List<String> scanLibraries(){
        List<String> result=new ArrayList();
        File[] f =new File(this.root+"/libraries").listFiles();
        for (File f2:f){
            result.add(f2.getAbsolutePath());
        }
        return result;
    }
}
