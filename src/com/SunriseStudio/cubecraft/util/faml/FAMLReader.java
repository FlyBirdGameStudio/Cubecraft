package com.SunriseStudio.cubecraft.util.faml;
import java.util.ArrayList;
import java.util.List;

public class FAMLReader {
    private String Doc;
    private ArrayList<String> Lists = new ArrayList<>();
    public FAMLReader(String Docs){
        Doc = Docs;
    }

    public void BuildDocs(){
        for (String Doc1 : Doc.split("|")){
            Lists.add(Doc1);
        }
        System.gc();
    }

    public String GetStringValue(String Key){
        if(Lists.contains(Key)) return Lists.get(Lists.indexOf(Key) + 1);
        else return null;
    }

    public Boolean GetBooleanValue(String Key){
        if(Lists.contains(Key)){
            if(Lists.get(Lists.indexOf(Key) + 1).equals("true")) return true;
            else return false;
        }else return null;
    }

    public Integer GetIntegerValue(String Key){
        if(Lists.contains(Key)){
            try{
                return Integer.parseInt(Lists.get(Lists.indexOf(Key) + 1));
            }catch (Exception e) {
                return null;
            }
        }return null;
    }
}
