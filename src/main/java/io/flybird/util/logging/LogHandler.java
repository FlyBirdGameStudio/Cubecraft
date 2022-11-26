package io.flybird.util.logging;

import com.google.gson.GsonBuilder;
import io.flybird.util.ColorUtil;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogHandler {
    private static String logPath = "/";

    private static final HashMap<String, ArrayList<Log>> logArrays = new HashMap<>();
    public static HashMap<String, File> files = new HashMap<>();
    public static HashMap<String, LogHandler> handlers = new HashMap<>();

    private static LogFormat logFormat = new LogFormat(
            " [{time}][{type}]{source} : {msg}",
            " [{time}][{type}]{source} : {msg}",
            " [{time}][{type}]{source} : {msg}",
            " [{time}][{type}]{source} : {msg}",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd_HH-mm-ss"
    );

    public static void setLogFormat(String loc){
        try {
            InputStream stream=new FileInputStream(loc);
            String s=new String(stream.readAllBytes());
           setLogFormat(
                   new GsonBuilder().registerTypeAdapter(LogFormat.class,new LogFormat.Deserializer())
                           .create().fromJson(s,LogFormat.class)
           );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLogFormat(LogFormat logFormat) {
        LogHandler.logFormat = logFormat;
    }

    public static LogHandler create(String source, String file) {
        if (logOutput) {
            if (!files.containsKey(file)) {
                SimpleDateFormat formatter = new SimpleDateFormat(logFormat.fileFormat());
                String prefix = file + "_";
                String path = logPath + (Objects.equals(file, "_NO_") ? "" : prefix) + formatter.format(new Date(System.currentTimeMillis())) + ".log";
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
        LogHandler e = new LogHandler(source, file);
        handlers.put(file, e);
        return e;
    }

    public static void allSave() {
        if (logOutput) {
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
                        writer.write(e.getOutputMessage() + "\n");
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

    private static boolean logOutput = true;

    public String source;
    public String file;

    private LogHandler(String source, String file) {
        this.source = source;
        this.file = file;
    }

    public static void setLogOutput(boolean logOutput) {
        LogHandler.logOutput = logOutput;
    }

    public static LogHandler create(String name) {
        return create(name, "_NO_");
    }

    public void info(String str,Object... args ){
        log(str.formatted(args), LogType.INFO);
    }

    public void warning(String str,Object... args) {
        log(str.formatted(args), LogType.WARN);
    }

    public void error(String str,Object... args) {
        log(str.formatted(args), LogType.ERROR);
    }

    public void exception(String msg,Object... args){
        this.log(msg.formatted(args),LogType.EXCEPTION);
    }

    public void checkGLError(String status) {
        int errorStatus = GL11.glGetError();
        if (errorStatus != 0) {
            throw new RuntimeException(errorStatus + ":" + status);
        }
    }

    public void log(String message, LogType type) {
        Log e = new Log(message, type, System.currentTimeMillis(), this.source);
        if (logOutput) {
            logArrays.get(this.file).add(e);
        }
        System.out.println(e.getFormattedMassage());
    }


    public record Log(String message, LogType err, long time, String source) implements Comparable<Log> {
        @Override
        public int compareTo(Log o) {
            return o.time >= this.time ? 1 : -1;
        }

        public String getFormattedMassage() {
            Date date = new Date(time);
            return LogHandler.logFormat.format(source, message, err, date);
        }

        public String getOutputMessage() {
            Date date = new Date(time);
            return LogHandler.logFormat.formatWriting(source, message, err, date);
        }
    }

    public static void setLogPath(String logPath) {
        LogHandler.logPath = logPath;
    }

    public static LogFormat getLogFormat() {
        return logFormat;
    }

    public static String getLogPath() {
        return logPath;
    }

    public void error(Error e){
        this.warning("|> Error Was Found : %s", e.getLocalizedMessage());
        this.error("|> Maybe Cause : %s", e.getCause());
        for(StackTraceElement line : e.getStackTrace())
            this.error(
                    "|- at %s.%s (at line %s from file %s) ",
                    line.getClassName(),
                    line.getMethodName(),
                    line.getLineNumber(),
                    line.getFileName()
            );
    }

    public void exception(Exception e){
        this.exception("|> Error Was Found : %s", e.getLocalizedMessage());
        this.exception("|> Maybe Cause : %s", e.getCause());
        for(StackTraceElement line : e.getStackTrace())
            this.exception(
                    "|- at %s.%s (at line %s from file %s) ",
                    line.getClassName(),
                    line.getMethodName(),
                    line.getLineNumber(),
                    line.getFileName()
            );
    }
}