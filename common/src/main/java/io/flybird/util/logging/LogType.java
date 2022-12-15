package io.flybird.util.logging;

enum LogType {
    INFO("info"),
    WARN("warning"),
    ERROR("error"),
    EXCEPTION("exception");

    final String desc;

    LogType(String desc) {
        this.desc = desc;
    }
}
