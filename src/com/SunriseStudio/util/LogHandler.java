package com.sunrisestudio.util;

import com.sunrisestudio.cubecraft.Start;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogHandler {
    private static final HashMap<String, ArrayList<Log>> logArrays=new HashMap<>();
    public static HashMap<String,File> files=new HashMap<>();
    public static HashMap<String,LogHandler> handlers=new HashMap<>();
    public static LogHandler create(String source,String file){
        if(logOutput) {
            if (!files.containsKey(file)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String path = Start.getGamePath() + "/logs/" + file + "_" + formatter.format(new Date(System.currentTimeMillis())) + ".log";
                File logFile = new File(path);
                logFile.getParentFile().mkdir();
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                files.put(file, logFile);
                logArrays.put(file, new ArrayList<>());
            }
        }
        LogHandler e=new LogHandler(source,file);
        handlers.put(file,e);
        return e;
    }
    public static void allSave() {
        if(logOutput) {
            Set<String> keySet = logArrays.keySet();
            for (String s : keySet) {
                FileWriter writer;
                try {
                    writer = new FileWriter(files.get(s));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                logArrays.get(s).sort(Comparator.comparingLong(o -> o.time));
                for (Log e : logArrays.get(s)) {
                    try {
                        writer.write(e.getFormattedMassage() + "\n");
                    } catch (IOException ex) {
                        System.out.println("can not write log:" + e);
                    }
                }
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private static boolean logOutput=true;

    public String source;
    public String file;
    private LogHandler(String source,String file){
        this.source=source;
        this.file=file;
    }

    public static void setLogOutput(boolean logOutput) {
        LogHandler.logOutput = logOutput;
    }

    public void info(String str){
        log(str,Errors.INFO);
    }
    public void warning(String str){
        log(str,Errors.WARNING);
    }
    public void error(String str){
        log(str,Errors.ERROR);
    }
    public void checkGLError(String status){
        int errorStatus= GL11.glGetError();
        if (errorStatus != 0) {
            throw new RuntimeException(errorStatus+":"+status);
        }
    }
    public void exception(Exception e){
        log(e.getMessage(),Errors.ERROR);
    }
    public void log(String message,Errors error) {
        Log e = new Log(message, error, System.currentTimeMillis(), this.source);
        if (logOutput) {
            logArrays.get(this.file).add(e);
        }
        System.out.println(e.getFormattedMassage());
    }


    public record Log(String message,Errors err,long time,String source) implements Comparable<Log>{
        @Override
        public int compareTo(Log o) {
            return o.time>=this.time?1:-1;
        }

        public String getFormattedMassage(){
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            return err.color+"[" + formatter.format(date) + "]" +
                    "[" + err.name + "]" +
                    "[" + source + "]" +StringColorConstants.WHITE+
                    message;
        }
    }

    public enum Errors{
        INFO("INFO", StringColorConstants.GREEN),
        WARNING("WARNING", StringColorConstants.YELLOW),
        ERROR("ERROR",StringColorConstants.RED);

        public final String name;

        public final String color;

        Errors(String error, String color) {
            this.name=error;
            this.color=color;
        }
    }


}