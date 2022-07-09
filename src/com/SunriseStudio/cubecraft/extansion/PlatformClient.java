package com.sunrisestudio.cubecraft.extansion;

import com.sunrisestudio.cubecraft.CubeCraft;
import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.gui.screen.Screen;
import com.sunrisestudio.cubecraft.net.ClientIO;
import com.sunrisestudio.cubecraft.world.access.IWorldAccess;

import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;

public record PlatformClient(
        CubeCraft platform,
        ClientIO clientNettyChannel,
        Screen currentScreen,
        DisplayScreenInfo displayInfo,
        IWorldAccess worldAccess,
        Player player,
        PlatformBase platformBase
) {
}
