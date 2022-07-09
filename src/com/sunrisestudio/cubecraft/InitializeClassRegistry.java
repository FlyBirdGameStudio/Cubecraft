package com.sunrisestudio.cubecraft;

import com.sunrisestudio.cubecraft.world.block.registery.BlockMap;
import com.sunrisestudio.cubecraftcontent.BlockBehaviors;
import com.sunrisestudio.cubecraftcontent.Blocks;
import com.sunrisestudio.cubecraft.world.entity.EntityMap;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;

public class InitializeClassRegistry {
    public static void registerEntity(){
        EntityMap.getInstance().register("player", Player.class);

    }

    public static void registerBlock(){
        BlockMap.getInstance().addRegistererBlockBehaviorClass(BlockBehaviors.class);
        BlockMap.getInstance().addRegistererBlockClass(Blocks.class);

    }
}
