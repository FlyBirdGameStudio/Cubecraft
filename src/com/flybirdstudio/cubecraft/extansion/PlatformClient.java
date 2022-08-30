package com.flybirdstudio.cubecraft.extansion;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.cubecraft.world.entity.humanoid.Player;
import com.flybirdstudio.util.net.UDPSocket;

public record PlatformClient(
        Cubecraft platform,
        UDPSocket clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        IWorld worldAccess,
        Player player
) {
}
