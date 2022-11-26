package io.flybird.cubecraft.internal.world.provider;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.world.*;

public class WorldProviderOverWorld implements IWorldProvider {
    @Override
    public ClientWorld createClientWorld(Level level) {
        //todo:world provider
        return null;
    }

    @Override
    public ServerWorld createServerWorld(Level level, GameSetting setting) {
        //todo:world provider
        return new ServerWorld("cubecraft:overworld",level.getInfo(),setting);
    }
}
