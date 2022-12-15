package io.flybird.util;

import io.flybird.util.container.CollectionUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class GameSetting {
    private final File file;
    public final HashMap<String, Object> buffer = new HashMap<>();
    public final Properties properties = new Properties();
    private final String operator;

    public GameSetting(String path,String operator) {
        this.file = new File(path);
        this.operator=operator;
        if (!this.file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public Object getValue(String path, Object _default) {
        return this.properties.getOrDefault(path, _default);
    }

    public int getValueAsInt(String path, int i) {
        return Integer.parseInt((String) this.properties.getOrDefault(path, String.valueOf(i)));
    }

    public boolean getValueAsBoolean(String path, boolean b) {
        return Boolean.parseBoolean((String) this.properties.getOrDefault(path, String.valueOf(b)));
    }

    public void setValue(String path, Object data) {
        buffer.put(path, data);
    }

    public void setValueNoBuffer(String path, Object data) {
        this.properties.put(path, data);
    }

    public void flush() {
        CollectionUtil.iterateMap(this.buffer, properties::put);
        this.buffer.clear();
    }

    public void read() {
        InputStream in;
        try {
            in = new FileInputStream(file);
            this.properties.load(in);
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            this.properties.store(out,"[created by %s]".formatted(operator));
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int FXAA = 0;
}
