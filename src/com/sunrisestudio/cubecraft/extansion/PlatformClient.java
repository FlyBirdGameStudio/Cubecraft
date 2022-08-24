package com.sunrisestudio.cubecraft.extansion;

import com.sunrisestudio.cubecraft.client.Cubecraft;
import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.screen.Screen;
import com.sunrisestudio.cubecraft.net.ClientIO;

import com.sunrisestudio.cubecraft.world.World;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;
import com.sunrisestudio.util.net.UDPSocket;

public record PlatformClient(
        Cubecraft platform,
        UDPSocket clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        World worldAccess,
        Player player
) {
}
