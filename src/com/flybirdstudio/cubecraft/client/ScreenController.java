package com.flybirdstudio.cubecraft.client;

import com.flybirdstudio.cubecraft.client.event.ScreenInitializeEvent;
import com.flybirdstudio.cubecraft.client.gui.Popup;
import com.flybirdstudio.cubecraft.client.gui.ScreenLoader;
import com.flybirdstudio.cubecraft.client.gui.ScreenUtil;
import com.flybirdstudio.cubecraft.client.gui.component.Button;
import com.flybirdstudio.cubecraft.client.gui.component.Label;
import com.flybirdstudio.cubecraft.client.gui.screen.HUDScreen;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.cubecraft.extansion.ModManager;
import com.flybirdstudio.cubecraft.extansion.PluginManager;
import com.flybirdstudio.util.event.EventHandler;
import com.flybirdstudio.cubecraft.world.LevelInfo;
import com.flybirdstudio.cubecraft.world.ServerWorld;
import com.flybirdstudio.util.event.EventListener;
import com.flybirdstudio.util.file.lang.Language;

import java.util.Date;
import java.util.Objects;

public class ScreenController implements EventListener {

    @EventHandler
    public void onButtonClicked(Button.ActionEvent e){
        if(Objects.equals(e.component().getParent().getID(), "cubecraft:title_screen")) {
            if (Objects.equals(e.component().getID(), "button_singleplayer")){
                e.component().getParent().getPlatform().setScreen(new HUDScreen());
                e.component().getParent().getPlatform().joinWorld(new ServerWorld(new LevelInfo("NULL", "NULL", 0, new Date(), false, "NULL", null)));
            }
            if (Objects.equals(e.component().getID(), "button_multiplayer")) {
                ScreenUtil.createPopup("?", "function not implemented!", 60, Popup.WARNING);
            }
            if(Objects.equals(e.component().getID(), "button_option")){
                Screen screen= ScreenLoader.loadByExtName("/resource/ui/setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("/resource/ui/title_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if(Objects.equals(e.component().getID(), "button_quit")){
                e.component().getParent().getPlatform().stop();
            }
        }
        if(Objects.equals(e.component().getParent().getID(), "cubecraft:pause_screen")) {
            if (Objects.equals(e.component().getID(), "button_back")) {
                 e.component().getParent().getPlatform().setScreen(new HUDScreen());
            }
            if (Objects.equals(e.component().getID(), "button_option")) {
                Screen screen= ScreenLoader.loadByExtName("/resource/ui/setting_screen.xml");
                screen.setParentScreen(ScreenLoader.loadByExtName("/resource/ui/pause_screen.xml"));
                e.component().getParent().getPlatform().setScreen(screen);
            }
            if (Objects.equals(e.component().getID(), "button_open_to_net")) {

            }
            if (Objects.equals(e.component().getID(), "button_achievement")) {

            }
            if (Objects.equals(e.component().getID(), "button_save_and_quit")){
                e.component().getParent().getPlatform().leaveWorld();
                e.component().getParent().getPlatform().setScreen("/resource/ui/title_screen.xml");
            }
        }
        if(Objects.equals(e.component().getParent().getID(), "cubecraft:setting_screen")) {
            if (Objects.equals(e.component().getID(), "confirm")) {
                e.component().getParent().getPlatform().setScreen(e.component().getParent().getParentScreen());
            }
        }
    }

    @EventHandler
    public void onScreenInit(ScreenInitializeEvent e){
        if(Objects.equals(e.screen().getID(),"cubecraft:title_screen")){
            ((Label) e.screen().getComponents().get("version_string")).text.setText(Language.getFormattedMessage(
                    "title_screen.version", Cubecraft.VERSION, ModManager.getLoadedMods().size(), PluginManager.getLoadedPlugins().size()
            ));
            ((Label) e.screen().getComponents().get("auth_string")).text.setText(Language.getFormattedMessage(
                    "title_screen.auth", "(NoAuthInfo)"
            ));
        }
    }
}
