package com.sunrisestudio.cubecraft.client;

import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.input.Keyboard;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;

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
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
            if (this.client.getScreen().getParentScreen() != null) {
                this.client.setScreen(this.client.getScreen().getParentScreen());
            }
        }
    }
}