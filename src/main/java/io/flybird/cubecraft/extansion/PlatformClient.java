package io.flybird.cubecraft.extansion;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.screen.Screen;

import io.flybird.cubecraft.net.base.ClientNettyPipeline;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.entity.humanoid.Player;

public record PlatformClient(
        Cubecraft platform,
        ClientNettyPipeline clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        IWorld worldAccess,
        Player player
) {
}
