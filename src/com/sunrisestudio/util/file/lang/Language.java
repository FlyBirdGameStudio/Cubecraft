package com.sunrisestudio.util.file.lang;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Language {
    public static Language selectedLanguage;
    public static HashMap<LanguageType,Language> instances=new HashMap<>();


    public static Language getInstance() {
        return selectedLanguage;
    }
    public static Language selectInstance(LanguageType type) {
        selectedLanguage=instances.get(type);
        return selectedLanguage;
    }
    public static Language create(LanguageType languageType){
        instances.put(languageType,new Language());
        return selectInstance(languageType);
    }

    private Language(){}


    public HashMap<String,String> map=new HashMap<>();

    public static String get(String s) {
        return selectedLanguage.map.getOrDefault(s,s);
    }

    public void attachTranslationFile(InputStream in){
        this.map.putAll(getLangFromStream(in));
    }

    public void clear(){
        this.map.clear();
    }

    public static HashMap<String,String> getLangFromStream(InputStream inputStream){
        HashMap<String,String> result=new HashMap<>();
        byte[] b;
        try {
            b=inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String s=new String(b, StandardCharsets.UTF_8);

        String[] str=s.split("\n");

        for (String arg:str){
            if(!arg.contains("#")) {
                StringBuilder sb = new StringBuilder();
                String k = null;
                String v;
                for (char c : arg.toCharArray()) {
                    if (c == '=') {
                        k = sb.toString();
                        sb = new StringBuilder();
                    } else {
                        sb.append(c);
                    }

                }
                v = sb.toString();
                result.put(k, v);
            }
        }
        return result;
    }

    public enum LanguageType{
        ZH_CN("中文简体"),
        ZH_TW("中文繁体"),
        EN_US("English");

        final String display;
        LanguageType(String display) {
            this.display=display;
        }
    }

    public String getFormattedMessage(String path,Object... args){
        return selectedLanguage.map.getOrDefault(path,path).formatted(args);
    }
}
