package io.flybird.cubecraft.internal;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.base.Popup;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.starfish3d.event.KeyPressEvent;
import io.flybird.cubecraft.client.gui.component.Screen;
import io.flybird.starfish3d.platform.Keyboard;
import io.flybird.util.event.EventHandler;
import io.flybird.util.event.EventListener;

public class ClientInputHandler implements EventListener {
    private final Cubecraft client;

    public ClientInputHandler(Cubecraft client) {
        this.client = client;
    }

    @EventHandler
    public void onKeyEventPressed(KeyPressEvent e) {
        if (e.key() == Keyboard.KEY_F11) {
            this.client.getWindow().setWindowFullscreen(!this.client.getWindow().isWindowFullscreen());
        }
        if (e.key() == Keyboard.KEY_F12) {
            Screen screen=this.client.getScreen();
            this.client.getLoadingScreen().intro();
            ClientRegistries.RESOURCE_MANAGER.reload(this.client);
            this.client.setScreen(screen);
            this.client.getLoadingScreen().dispose();
        }
        if (e.key() == Keyboard.KEY_ESCAPE) {
            if (this.client.getScreen().getParentScreen() != null) {
                this.client.setScreen(this.client.getScreen().getParentScreen());
            }
        }
        if(e.key()==Keyboard.KEY_F9){
            ScreenUtil.createPopup("reloading...","reloading...",40, Popup.INFO);
            ClientRegistries.RESOURCE_MANAGER.reload(this.client);
            ScreenUtil.createPopup("reload success","fully reloaded.",40,Popup.SUCCESS);
        }
        if(e.key()==Keyboard.KEY_F3){
            client.isDebug=!client.isDebug;
        }
    }
}