package io.flybird.util.logging;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * simple log handler(thread save)
 *
 * @author GrassBlock2022
 */
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

    /**
     * set current log format
     *
     * @param loc format file location
     */
    public static void setLogFormat(String loc) {
        try {
            InputStream stream = new FileInputStream(loc);
            String s = new String(stream.readAllBytes());
            setLogFormat(
                    new GsonBuilder().registerTypeAdapter(LogFormat.class, new LogFormat.Deserializer())
                            .create().fromJson(s, LogFormat.class)
            );
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set current log format
     *
     * @param logFormat format
     */
    public static void setLogFormat(LogFormat logFormat) {
        LogHandler.logFormat = logFormat;
    }

    /**
     * create a logger
     *
     * @param source source
     * @param file   target file
     * @return brand-new logger
     */
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

    /**
     * all save log,clear cache.
     */
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

    /**
     * set if log should save to file,use when debugging.
     *
     * @param logOutput output
     */
    public static void setLogOutput(boolean logOutput) {
        LogHandler.logOutput = logOutput;
    }

    /**
     * create logger,using default file.
     *
     * @param name name
     * @return brand-new logger
     */
    public static LogHandler create(String name) {
        return create(name, "_NO_");
    }


    private static boolean logOutput = true;

    public String source;
    public String file;

    private LogHandler(String source, String file) {
        this.source = source;
        this.file = file;
    }


    /**
     * record info
     *
     * @param str message
     * @param args format arguments
     */
    public void info(String str, Object... args) {
        log(str.formatted(args), LogType.INFO);
    }

    /**
     * record warning
     *
     * @param str  message
     * @param args format arguments
     */
    public void warning(String str, Object... args) {
        log(str.formatted(args), LogType.WARN);
    }

    /**
     * record error
     *
     * @param str  message
     * @param args format arguments
     */
    public void error(String str, Object... args) {
        log(str.formatted(args), LogType.ERROR);
    }

    /**
     * record exception
     *
     * @param msg  message
     * @param args format arguments
     */
    public void exception(String msg, Object... args) {
        this.log(msg.formatted(args), LogType.EXCEPTION);
    }

    /**
     * write info by type
     *
     * @param message msg
     * @param type    type
     */
    public void log(String message, LogType type) {
        Log e = new Log(message, type, System.currentTimeMillis(), this.source);
        if (logOutput) {
            logArrays.get(this.file).add(e);
        }
        System.out.println(e.getFormattedMassage());
    }


    record Log(String message, LogType err, long time, String source) implements Comparable<Log> {
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

    /**
     * set log location
     *
     * @param logPath path
     */
    public static void setLogPath(String logPath) {
        LogHandler.logPath = logPath;
    }

    /**
     * create stack trace and log error
     *
     * @param e obj
     * @author CubeVlmu
     */
    public void error(Error e) {
        this.warning("|> Error Was Found : %s", e.getLocalizedMessage());
        this.error("|> Maybe Cause : %s", e.getCause());
        for (StackTraceElement line : e.getStackTrace())
            this.error(
                    "|- at %s.%s (at line %s from file %s) ",
                    line.getClassName(),
                    line.getMethodName(),
                    line.getLineNumber(),
                    line.getFileName()
            );
    }

    /**
     * create stack trace and log exception
     *
     * @param e obj
     * @author CubeVlmu
     */
    public void exception(Exception e) {
        this.exception("|> Error Was Found : %s", e.getLocalizedMessage());
        this.exception("|> Maybe Cause : %s", e.getCause());
        for (StackTraceElement line : e.getStackTrace())
            this.exception(
                    "|- at %s.%s (at line %s from file %s) ",
                    line.getClassName(),
                    line.getMethodName(),
                    line.getLineNumber(),
                    line.getFileName()
            );
    }
}