package io.flybird.cubecraft.client;

import io.flybird.cubecraft.client.gui.screen.Screen;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.platform.input.Keyboard;
import io.flybird.starfish3d.platform.input.KeyboardCallback;
import io.flybird.starfish3d.platform.input.MouseCallBack;

public class ClientInputHandler implements KeyboardCallback, MouseCallBack {
    private final Cubecraft client;

    public ClientInputHandler(Cubecraft client) {
        this.client = client;
    }

    @Override
    public void onKeyEventPressed() {
        if (Keyboard.getEventKey() == Keyboard.KEY_F11) {
            Display.setFullscreen(!Display.isFullscreen());
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_F12) {
            Screen screen=this.client.getScreen();
            this.client.getLoadingScreen().intro();
            ResourceManager.instance.reload(this.client);
            this.client.setScreen(screen);
            this.client.getLoadingScreen().dispose();
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
            if (this.client.getScreen().getParentScreen() != null) {
                this.client.setScreen(this.client.getScreen().getParentScreen());
            }
        }
    }
}