package com.sunrisestudio.cubecraft.extansion;

import com.sunrisestudio.cubecraft.client.CubeCraft;
import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.screen.Screen;
import com.sunrisestudio.cubecraft.net.ClientIO;
import com.sunrisestudio.cubecraft.world.IWorldAccess;

import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;

public record PlatformClient(
        CubeCraft platform,
        ClientIO clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        IWorldAccess worldAccess,
        Player player
) {
}
