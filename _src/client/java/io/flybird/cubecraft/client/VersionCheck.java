package io.flybird.cubecraft.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.flybird.cubecraft.client.gui.base.Popup;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.register.Registries;
import io.flybird.util.HTTPUtil;
import io.flybird.util.I18nHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class VersionCheck implements Runnable {

    public static final String CHECK_VERSION_URL = "https://api.github.com/repos/FlyBirdGameStudio/Cubecraft/releases/tags/Client";

    private final Logger logger = LogManager.getLogger("VersionCheck");

    @Override
    public void run() {
        I18nHelper i18nHelper = Registries.I18N;
        try {
            String request = HTTPUtil.get(CHECK_VERSION_URL);
            JsonObject obj = JsonParser.parseString(request).getAsJsonObject();
            String version = obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String updateAt = obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("updated_at").getAsString();
            String download = obj.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
            String info = obj.get("body").getAsString();

            version = version.replace(".jar", "");

            int id = compare(version, Cubecraft.VERSION);
            if (id == UNKNOWN_REMOTE) {
                ScreenUtil.createPopup(
                        i18nHelper.get("version_check.unknown_remote.title"),
                        i18nHelper.get("version_check.unknown_remote.subtitle", version),
                        60, Popup.INFO
                );
            }
            if (id == UNKNOWN_CLIENT) {
                ScreenUtil.createPopup(
                        i18nHelper.get("version_check.unknown_client.title"),
                        i18nHelper.get("version_check.unknown_client.subtitle", Cubecraft.VERSION),
                        60, Popup.WARNING
                );
            }
            if (id == UNEXPECTED_NEWER) {
                ScreenUtil.createPopup(
                        i18nHelper.get("version_check.unexpected_client.title"),
                        i18nHelper.get("version_check.unexpected_client.subtitle", Cubecraft.VERSION),
                        60, Popup.WARNING
                );
            }
            if (id == LATEST) {
                ScreenUtil.createPopup(
                        i18nHelper.get("version_check.latest.title"),
                        i18nHelper.get("version_check.latest.subtitle", Cubecraft.VERSION),
                        60, Popup.SUCCESS
                );
            }
            if (id == OLDER) {
                ScreenUtil.createPopup(
                        i18nHelper.get("version_check.latest.title"),
                        i18nHelper.get("version_check.latest.subtitle", Cubecraft.VERSION, version),
                        60, Popup.INFO
                );
            }
        } catch (IOException e) {
            logger.catching(e);
            ScreenUtil.createPopup(
                    i18nHelper.get("version_check.exception.title"),
                    i18nHelper.get("version_check.exception.subtitle"),
                    60, Popup.ERROR
            );
        }
    }


    public static final int UNKNOWN_REMOTE = 404;
    public static final int UNKNOWN_CLIENT = 303;
    public static final int UNEXPECTED_NEWER = 1;
    public static final int LATEST = 0;
    public static final int OLDER = -1;

    public int compare(String version, String target) {
        String[] num = version.split("\\.");
        String[] current = target.split("\\.");

        int[] numI = new int[3];

        try {
            for (int i = 0; i < 3; i++) {
                numI[i] = Integer.parseInt(num[i]);
            }
        } catch (Exception e) {
            return UNKNOWN_REMOTE;
        }
        if (current.length != 3) {
            return UNKNOWN_CLIENT;
        } else if (num.length != 3) {
            return UNKNOWN_REMOTE;
        } else {
            return LATEST;
        }
    }

    public static void check() {
        new Thread(new VersionCheck(), "client_version_check").start();
    }
}
