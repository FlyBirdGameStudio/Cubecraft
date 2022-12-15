package io.flybird.util.logging;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.flybird.util.ColorUtil;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

record LogFormat(
        String info,
        String warn,
        String error,
        String exception,
        String timeFormat,
        String fileFormat
) {
    public String format(String source, String msg, LogType type, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        String selectedTemplete=switch (type){
            case INFO -> info;
            case WARN -> warn;
            case ERROR -> error;
            case EXCEPTION -> exception;
        };
        return selectedTemplete
                .replace("{type}",type.desc)
                .replace("{time}",formatter.format(date))
                .replace("{source}",source)
                .replace("{msg}",msg)
                .replace("{default}", ColorUtil.DEFAULT)
                .replace("{black}", ColorUtil.BLACK)
                .replace("{red}",ColorUtil.RED)
                .replace("{green}",ColorUtil.GREEN)
                .replace("{yellow}",ColorUtil.YELLOW)
                .replace("{blue}",ColorUtil.BLUE)
                .replace("{purple}",ColorUtil.PURPLE)
                .replace("{cyan}",ColorUtil.CYAN)
                .replace("{white}",ColorUtil.WHITE);
    }

    public String formatWriting(String source, String msg, LogType type, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        String selectedTemplate=switch (type){
            case INFO -> info;
            case WARN -> warn;
            case ERROR -> error;
            case EXCEPTION -> exception;
        };
        return selectedTemplate
                .replace("{type}",type.desc)
                .replace("{time}",formatter.format(date))
                .replace("{source}",source)
                .replace("{msg}",msg)
                .replace("{default}", "")
                .replace("{black}", "")
                .replace("{red}","")
                .replace("{green}","")
                .replace("{yellow}","")
                .replace("{blue}","")
                .replace("{purple}","")
                .replace("{cyan}","")
                .replace("{white}","");
    }

    public static class Deserializer implements JsonDeserializer<LogFormat>{

        @Override
        public LogFormat deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new LogFormat(
                    json.getAsJsonObject().get("line").getAsJsonObject().get("info").getAsString(),
                    json.getAsJsonObject().get("line").getAsJsonObject().get("warning").getAsString(),
                    json.getAsJsonObject().get("line").getAsJsonObject().get("error").getAsString(),
                    json.getAsJsonObject().get("line").getAsJsonObject().get("exception").getAsString(),
                    json.getAsJsonObject().get("time").getAsJsonObject().get("line").getAsString(),
                    json.getAsJsonObject().get("time").getAsJsonObject().get("file").getAsString()
            );
        }
    }
}
