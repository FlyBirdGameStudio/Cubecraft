package io.flybird.cubecraft.internal.world.provider;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.GameSetting;
import io.flybird.cubecraft.internal.type.WorldType;
import io.flybird.cubecraft.world.ClientWorld;
import io.flybird.cubecraft.world.IWorldProvider;
import io.flybird.cubecraft.world.Level;
import io.flybird.cubecraft.world.ServerWorld;

public class WorldProviderOverWorld implements IWorldProvider {
    @Override
    public ClientWorld createClientWorld(Cubecraft client) {
        return new ClientWorld(client.getClientLevelInfo(),client, WorldType.OVERWORLD);
    }

    @Override
    public ServerWorld createServerWorld(Level level, GameSetting setting) {
        return new ServerWorld("cubecraft:overworld",level,level.getInfo(),setting);
    }
}
