package io.flybird.cubecraft.internal.world.provider;

import io.flybird.cubecraft.world.ClientWorld;
import io.flybird.cubecraft.world.IWorldProvider;
import io.flybird.cubecraft.world.Level;
import io.flybird.cubecraft.world.ServerWorld;

public class WorldProviderOverWorld implements IWorldProvider {
    @Override
    public ClientWorld createClientWorld(Level level) {
        //todo:world provider
        return null;
    }

    @Override
    public ServerWorld createServerWorld(Level level, String name) {
        //todo:world provider
        return null;
    }
}
