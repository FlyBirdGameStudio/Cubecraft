package io.flybird.cubecraft.extansion;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.screen.Screen;

import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.util.net.UDPSocket;

public record PlatformClient(
        Cubecraft platform,
        UDPSocket clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        IWorld worldAccess,
        Player player
) {
}
