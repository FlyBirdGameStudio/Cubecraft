package io.flybird.cubecraft.register;

import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.block.OverwrittenBlock;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

public class RegisterUtil {
    public static void registerDefaultOverrideBlock(String id, String behavior, NameSpacedRegisterMap<Block, Block> blockMap){
        blockMap.registerItem(id,new OverwrittenBlock(id, Registries.BLOCK_BEHAVIOR.get(behavior)));
    }

}
