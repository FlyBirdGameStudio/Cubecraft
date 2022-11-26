package io.flybird.util.logging;

public enum LogType {
    INFO("info",0),
    WARN("warning",1),
    ERROR("error",2),
    EXCEPTION("exception",3);

    final String desc;
    final int level;

    LogType(String desc, int level) {
        this.desc = desc;
        this.level = level;
    }

    public boolean contains(LogType another){
        return another.level>this.level;
    }
}
