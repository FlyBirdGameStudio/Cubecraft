package io.flybird.cubecraft.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.flybird.cubecraft.client.gui.Popup;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.util.HTTPUtil;
import io.flybird.util.file.lang.Language;
import io.flybird.util.logging.LogHandler;

import java.io.IOException;
import java.util.Objects;


public class VersionCheck implements Runnable{
    public static final String CHECK_VERSION_URL="https://api.github.com/repos/FlyBirdGameStudio/Cubecraft/releases/tags/Client";

    @Override
    public void run() {
        LogHandler handler=LogHandler.create("VersionCheck");
        try {
            String request = HTTPUtil.get(CHECK_VERSION_URL);

            JsonObject obj=JsonParser.parseString(request).getAsJsonObject();
            String version=obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String updateAt=obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("updated_at").getAsString();
            String download=obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            String info=obj.get("body").getAsString();

            version=version.replace(".jar","");

            int id=compare(version,Cubecraft.VERSION);
            if(id==UNKNOWN_REMOTE){
                ScreenUtil.createPopup(
                        Language.get("version_check.unknown_remote.title"),
                        Language.getFormattedMessage("version_check.unknown_remote.subtitle",version),
                        60, Popup.INFO
                );
            }
            if(id==UNKNOWN_CLIENT){
                ScreenUtil.createPopup(
                        Language.get("version_check.unknown_client.title"),
                        Language.getFormattedMessage("version_check.unknown_client.subtitle",Cubecraft.VERSION),
                        60, Popup.WARNING
                );
            }
            if(id==UNEXPECTED_NEWER){
                ScreenUtil.createPopup(
                        Language.get("version_check.unexpected_client.title"),
                        Language.getFormattedMessage("version_check.unexpected_client.subtitle",Cubecraft.VERSION),
                        60, Popup.WARNING
                );
            }
            if(id==LATEST){
                ScreenUtil.createPopup(
                        Language.get("version_check.latest.title"),
                        Language.getFormattedMessage("version_check.latest.subtitle",Cubecraft.VERSION),
                        60, Popup.SUCCESS
                );
            }
            if(id==OLDER){
                ScreenUtil.createPopup(
                        Language.get("version_check.latest.title"),
                        Language.getFormattedMessage("version_check.latest.subtitle",Cubecraft.VERSION,version),
                        60, Popup.INFO
                );
            }
        } catch (IOException e) {
            handler.exception(e);
            ScreenUtil.createPopup(
                    Language.get("version_check.exception.title"),
                    Language.get("version_check.exception.subtitle"),
                    60, Popup.ERROR
            );
        }
    }


    public static final int UNKNOWN_REMOTE=404;
    public static final int UNKNOWN_CLIENT=303;
    public static final int UNEXPECTED_NEWER=1;
    public static final int LATEST=0;
    public static final int OLDER=-1;
    public int compare(String version,String target){
        String[] num=version.split("\\.");
        String[] current=target.split("\\.");

        int[] numI=new int[3];
        int[] remoteI=new int[3];

        try {
            for (int i = 0; i < 3; i++) {
                numI[i] = Integer.parseInt(num[i]);
            }
        }catch (Exception e){
            return UNKNOWN_REMOTE;
        }
        if(current.length!=3){
            return UNKNOWN_CLIENT;
        }else if(num.length!=3){
            return UNKNOWN_REMOTE;
        }else{
            return LATEST;
        }
    }

    public static void check(){
        new Thread(new VersionCheck(), "client_version_check").start();
    }
}
