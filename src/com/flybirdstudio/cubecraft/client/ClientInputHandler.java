package com.flybirdstudio.cubecraft.client;

import com.flybirdstudio.starfish3d.platform.Display;
import com.flybirdstudio.starfish3d.platform.input.Keyboard;
import com.flybirdstudio.starfish3d.platform.input.KeyboardCallback;
import com.flybirdstudio.starfish3d.platform.input.MouseCallBack;

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