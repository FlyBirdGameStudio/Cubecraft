package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.gui.Popup;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.util.HTTPUtil;
import io.flybird.util.file.lang.Language;

import java.io.IOException;
import java.util.Objects;

public class VersionCheck implements Runnable{
    @Override
    public void run() {
        try {
            String request = HTTPUtil.get("http://api.sunrisestudio.top/version_check?product=cubecraft_client");
            if (!Objects.equals(request, Cubecraft.VERSION)) {
                ScreenUtil.createPopup(
                        Language.get("versioncheck.exception.title"),
                        Language.getFormattedMessage("versioncheck.exception.subtitle", Cubecraft.VERSION, request),
                        60, Popup.ERROR
                );
            } else {
                ScreenUtil.createPopup(
                        Language.get("versioncheck.latest"),
                        Language.getFormattedMessage("versioncheck.latest.subtitle", request),
                        60, Popup.ERROR
                );
            }
        } catch (IOException e) {
            ScreenUtil.createPopup(
                    Language.get("versioncheck.exception.title"),
                    Language.get("versioncheck.exception.subtitle"),
                    60, Popup.ERROR
            );
        }
    }
}
