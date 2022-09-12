package com.flybirdstudio.cubecraft.client.resources;

import com.flybirdstudio.cubecraft.Start;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ResourcePack {
    private ZipFile file;
    private ArrayList<ZipEntry> entries=new ArrayList<>();

    /**
     * load file from disk，read all entries to file path
     * @param filePath abs path
     */
    public void load(String filePath) {
        try {
            filePath= Start.getGamePath()+"/resourcepacks/"+filePath;
            this.file = new ZipFile(new File(filePath));
            this.entries.clear();
            ZipInputStream inputStream=new ZipInputStream(new FileInputStream(filePath));
            ZipEntry e;
            while ((e= inputStream.getNextEntry())!=null){
                this.entries.add(e);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public InputStream getInput(String path) throws IOException {
        return this.file.getInputStream(this.file.getEntry(path));
    }
}