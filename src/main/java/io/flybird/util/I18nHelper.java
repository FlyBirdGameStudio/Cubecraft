package io.flybird.util;

import java.util.*;

/**
 * simple I18n helper
 *
 * @author GrassBlock2022
 */
public class I18nHelper {
    public static final String CHINESE_SIMPLIFIED = "zh-cn";
    public static final String ENGLISH = "en";
    public static final String ENGLISH_AMERICA = "en-us";

    /**
     * covert java locale format to our format.
     *
     * @param locale locale
     * @return sorted data format
     */
    public static String covert(Locale locale) {
        if (!Objects.equals(locale.getCountry(), "")) {
            return locale.getLanguage() + "-" + locale.getCountry().toLowerCase();
        }
        return locale.getLanguage();
    }

    private String currentType;

    public I18nHelper(String currentType) {
        this.currentType = currentType;
    }

    public I18nHelper() {
        this(CHINESE_SIMPLIFIED);
    }

    private final HashMap<String, LanguageTranslator> instances = new HashMap<>();

    /**
     * format string with given arg
     *
     * @param path      path
     * @param formatArg args
     * @return formatted arg
     */
    public String get(String path, Object... formatArg) {
        return this.instances.get(this.currentType).get(path, formatArg);
    }

    /**
     * attach file to current language type.
     *
     * @param raw file content
     */
    public void attach(String raw) {
        LanguageTranslator translator = this.instances.get(this.currentType);
        if (translator != null) {
            translator.attach(raw);
        }
    }

    /**
     * attach file to given language type.
     *
     * @param raw  file content
     * @param type type
     */
    public void attach(String type, String raw) {
        LanguageTranslator translator = this.instances.get(type);
        if (translator != null) {
            translator.attach(raw);
        }
    }

    /**
     * set current type.
     *
     * @param currentType type
     */
    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    /**
     * check current helper contains given type.
     *
     * @param type type
     * @return contains.
     */
    public boolean has(String type) {
        return this.instances.containsKey(type);
    }

    /**
     * add an author to current language.
     *
     * @param a name
     */
    public void addAuthor(String a) {
        LanguageTranslator translator = this.instances.get(this.currentType);
        if (translator != null) {
            translator.addAuthor(a);
        }
    }

    /**
     * add an author to given language.
     *
     * @param a    name
     * @param type type
     */
    public void addAuthor(String type, String a) {
        LanguageTranslator translator = this.instances.get(type);
        if (translator != null) {
            translator.addAuthor(a);
        }
    }

    /**
     * get authors of given language type.
     *
     * @param type type
     * @return list of authors.
     */
    public List<String> getAuthors(String type) {
        LanguageTranslator translator = this.instances.get(type);
        if (translator != null) {
            return translator.getAuthors();
        }
        return List.of();
    }

    /**
     * get authors of current language type.
     *
     * @return list of authors.
     */
    public List<String> getAuthors() {
        LanguageTranslator translator = this.instances.get(this.currentType);
        if (translator != null) {
            return translator.getAuthors();
        }
        return List.of();
    }

    /**
     * create new language type
     *
     * @param type    name
     * @param display display name
     */
    public void createNew(String type, String display) {
        this.instances.put(type, new LanguageTranslator(display));
    }

    static class LanguageTranslator {
        private final HashMap<String,String> mapping=new HashMap<>();
        private final ArrayList<String> authors=new ArrayList<>();
        private final String display;

        public LanguageTranslator(String display) {
            this.display = display;
        }

        public void attach(String s){
            String[] str=s.split("\n");

            for (String arg:str){
                StringBuilder sb=new StringBuilder();
                String k=null;
                String v;
                for (char c:arg.toCharArray()){
                    if(c=='='){
                        k=sb.toString();
                        sb=new StringBuilder();
                    }else{
                        sb.append(c);
                    }
                }
                v=sb.toString();
                this.mapping.put(k,v);
            }
        }

        public String get(String path,Object... formatArg){
            String raw=this.mapping.get(path);
            if(raw==null){
                return "ITEM_NOT_FOUND:%s".formatted(path);
            }
            try {
                return raw.formatted(formatArg);
            }catch (IllegalFormatException e){
                return "FORMAT_FAILED:%s(%s)".formatted(path,e.getMessage());
            }
        }

        public void addAuthor(String author){
            if(!this.authors.contains(author)){
                this.authors.add(author);
            }
        }

        public ArrayList<String> getAuthors() {
            return authors;
        }

        public HashMap<String, String> getMapping() {
            return mapping;
        }

        public String getDisplay() {
            return display;
        }
    }
}
