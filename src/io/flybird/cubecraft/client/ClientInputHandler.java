package io.flybird.cubecraft.client;

import io.flybird.starfish3d.event.KeyPressEvent;
import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.platform.input.Keyboard;
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
            Display.setFullscreen(!Display.isFullscreen());
        }
        if (e.key() == Keyboard.KEY_F12) {
            Screen screen=this.client.getScreen();
            this.client.getLoadingScreen().intro();
            ResourceManager.instance.reload(this.client);
            this.client.setScreen(screen);
            this.client.getLoadingScreen().dispose();
        }
        if (e.key() == Keyboard.KEY_ESCAPE) {
            if (this.client.getScreen().getParentScreen() != null) {
                this.client.setScreen(this.client.getScreen().getParentScreen());
            }
        }
    }
}