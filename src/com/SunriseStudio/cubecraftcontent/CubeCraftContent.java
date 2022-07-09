package com.sunrisestudio.cubecraftcontent;

import com.sunrisestudio.cubecraft.extansion.*;

public class CubeCraftContent extends Mod {
    public CubeCraftContent(PlatformClient client, PlatformServer server, PlatformBase platform, ExtansionRunningTarget target) {
        super(client, server, platform, target);
    }

    @Override
    public void construct() {
        this.getPlatformBase().blockMap().addRegistererBlockClass(Blocks.class);
        this.getPlatformBase().blockMap().addRegistererBlockBehaviorClass(BlockBehaviors.class);
    }
}
