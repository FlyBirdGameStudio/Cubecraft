package io.flybird.cubecraft.world;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.Cubecraft;

public interface IWorldProvider {
    ClientWorld createClientWorld(Cubecraft client);

    ServerWorld createServerWorld(Level level, GameSetting setting);
}