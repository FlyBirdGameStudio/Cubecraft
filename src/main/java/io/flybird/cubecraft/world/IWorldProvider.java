package io.flybird.cubecraft.world;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.util.GameSetting;

public interface IWorldProvider {
    ClientWorld createClientWorld(Cubecraft client);

    ServerWorld createServerWorld(Level level, GameSetting setting);
}