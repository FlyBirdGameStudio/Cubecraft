package io.flybird.cubecraft.client;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.event.ScreenInitializeEvent;
import io.flybird.cubecraft.client.gui.Popup;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.component.Label;
import io.flybird.cubecraft.client.gui.component.SplashText;
import io.flybird.cubecraft.client.gui.screen.HUDScreen;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.extansion.ModManager;
import io.flybird.cubecraft.extansion.PluginManager;
import io.flybird.cubecraft.world.LevelInfo;
import io.flybird.cubecraft.world.ServerWorld;
import io.flybird.util.event.EventHandler;
import io.flybird.util.event.EventListener;
import io.flybird.util.file.lang.Language;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ScreenController implements EventListener {

    @EventHandler
    public void onButtonClicked(Button.ActionEvent e) {
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:title_screen")) {
            if (Objects.equals(e.component().getID(), "button_singleplayer")) {
                e.component().getParent().getPlatform().setScreen(new HUDScreen());
                e.component().getParent().getPlatform().joinWorld(new ServerWorld(new LevelInfo("NULL", "NULL", 0, new Date(), false, "NULL", null), "overworld"));
            }
            if (Objects.equals(e.component().getID(), "button_multiplayer")) {
                ScreenUtil.createPopup("?", "function not implemented!", 60, Popup.WARNING);
            }
            if (Objects.equals(e.component().getID(), "button_option")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft","setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft","title_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_quit")) {
                e.component().getParent().getPlatform().stop();
            }
        }
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:pause_screen")) {
            if (Objects.equals(e.component().getID(), "button_back")) {
                e.component().getParent().getPlatform().setScreen(new HUDScreen());
            }
            if (Objects.equals(e.component().getID(), "button_option")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft","setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft","pause_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_open_to_net")) {

            }
            if (Objects.equals(e.component().getID(), "button_achievement")) {

            }
            if (Objects.equals(e.component().getID(), "button_save_and_quit")) {
                e.component().getParent().getPlatform().leaveWorld();
                e.component().getParent().getPlatform().setScreen("cubecraft","title_screen.xml");
            }
        }
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:setting_screen")) {
            if (Objects.equals(e.component().getID(), "confirm")) {
                e.component().getParent().getPlatform().setScreen(e.component().getParent().getParentScreen());
                GameSetting.instance.flush();
                GameSetting.instance.save();
            }
        }
    }

    @EventHandler
    public void onScreenInit(ScreenInitializeEvent e) {
        if (Objects.equals(e.screen().getID(), "cubecraft:title_screen")) {
            ((Label) e.screen().getComponents().get("version_string")).text.setText(Language.getFormattedMessage(
                    "title_screen.version", Cubecraft.VERSION, ModManager.getLoadedMods().size(), PluginManager.getLoadedPlugins().size()
            ));
            ((Label) e.screen().getComponents().get("auth_string")).text.setText(Language.getFormattedMessage(
                    "title_screen.auth", "(NoAuthInfo)"
            ));
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            if (c.get(Calendar.MONTH) == Calendar.OCTOBER && c.get(Calendar.DATE) == 23) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("Happy Birthday,FlyBirdStudio!");
            }
            if (c.get(Calendar.MONTH) == Calendar.JUNE && c.get(Calendar.DATE) == 1) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("Happy Birthday,Notch!");
            }
            if (c.get(Calendar.MONTH) == Calendar.MAY && c.get(Calendar.DATE) == 10) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("Happy Birthday,Minecraft!");
            }
            if (c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DATE) == 25) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("Happy Birthday,BugFuck!");
            }
        }
    }
}