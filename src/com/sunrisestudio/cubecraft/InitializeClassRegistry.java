package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;

public class InitializeClassRegistry {
    public static void registerEntity(){
        Registry.getEntityMap().registerClass("cubecraft","player", Player.class);

    }

}
