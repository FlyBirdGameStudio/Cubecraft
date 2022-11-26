package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.event.ScreenInitializeEvent;
import io.flybird.cubecraft.client.gui.Popup;
import io.flybird.cubecraft.client.gui.ScreenLoader;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.client.gui.component.Component;
import io.flybird.cubecraft.client.gui.component.Label;
import io.flybird.cubecraft.client.gui.component.SplashText;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.screen.HUDScreen;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.extansion.ModManager;
import io.flybird.cubecraft.register.Registry;
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
        //title screen
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:title_screen")) {
            //top-left buttons
            if (Objects.equals(e.component().getID(), "button_check_version")) {
                VersionCheck.check();
            }
            if (Objects.equals(e.component().getID(), "button_account_setting")) {
                VersionCheck.check();
            }

            //content buttons
            if (Objects.equals(e.component().getID(), "button_singleplayer")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft", "single_player_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft", "title_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_multiplayer")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft", "multi_player_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft", "title_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_option")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft", "setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft", "title_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_quit")) {
                e.component().getParent().getPlatform().stop();
            }
        }

        //single player screen
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:single_player_screen")) {
            if (Objects.equals(e.component().getID(), "join_world")) {
                Registry.getClient().joinLocalWorld("world");//todo:selection detect



                //e.component().getParent().getPlatform().setScreen(new HUDScreen());
                //e.component().getParent().getPlatform().joinWorld(new ServerWorld("cubecraft:overworld", new LevelInfo("NULL", "NULL", 0, new Date(), false, "NULL", null), e.component().getParent().getPlatform().getGameSetting()));
            }
        }

        //pause
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:pause_screen")) {
            if (Objects.equals(e.component().getID(), "button_back")) {
                e.component().getParent().getPlatform().setScreen(new HUDScreen());
            }
            if (Objects.equals(e.component().getID(), "button_option")) {
                Screen screen = ScreenLoader.loadByExtName("cubecraft", "setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("cubecraft", "pause_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_achievement")) {
                //todo:achievement screen
            }
            if (Objects.equals(e.component().getID(), "button_save_and_quit")) {
                e.component().getParent().getPlatform().leaveWorld();
                e.component().getParent().getPlatform().setScreen("cubecraft", "title_screen.xml");
            }
        }

        //option
        if (Objects.equals(e.component().getParent().getID(), "cubecraft:option_screen")) {
            if (Objects.equals(e.component().getID(), "confirm")) {
                e.component().getParent().getPlatform().setScreen(e.component().getParent().getParentScreen());
                ScreenUtil.createPopup(
                        Language.get("option.apply.title"),
                        Language.get("option.apply.subtitle"),
                80, Popup.SUCCESS);
                Registry.getClient().getGameSetting().flush();
                Registry.getClient().getGameSetting().save();
            }
            if (Objects.equals(e.component().getID(), "apply")) {
                ScreenUtil.createPopup(
                        Language.get("option.apply.title"),
                        Language.get("option.apply.subtitle"),
                80, Popup.SUCCESS);
                Registry.getClient().getGameSetting().flush();
                Registry.getClient().getGameSetting().save();
            }
            if (Objects.equals(e.component().getID(), "quit")) {
                e.component().getParent().getPlatform().setScreen(e.component().getParent().getParentScreen());
            }
        }
    }

    @EventHandler
    public void onScreenInit(ScreenInitializeEvent e) {
        if (Objects.equals(e.screen().getID(), "cubecraft:title_screen")) {
            ((Label) e.screen().getComponents().get("version_string")).text.setText(Language.getFormattedMessage(
                    "title_screen.version", Cubecraft.VERSION, ModManager.getLoadedMods().size()
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
            if (c.get(Calendar.MONTH) == Calendar.SEPTEMBER && c.get(Calendar.DATE) == 18) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("/");
                for (Component comp:e.screen().getComponents().values()){
                    if(comp instanceof Button){
                        ((Button) comp).enabled=false;
                    }
                }
            }
            if (c.get(Calendar.MONTH) == Calendar.DECEMBER && c.get(Calendar.DATE) == 13) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("/");
                for (Component comp:e.screen().getComponents().values()){
                    if(comp instanceof Button){
                        ((Button) comp).enabled=false;
                    }
                }
            }

            if (c.get(Calendar.MONTH) == Calendar.JULY && c.get(Calendar.DATE) == 8) {
                ((SplashText) e.screen().getComponents().get("splash")).getSplashText().setText("\"You won't be forgotten,You will be in the universe forever.\"");
            }

        }
    }
}