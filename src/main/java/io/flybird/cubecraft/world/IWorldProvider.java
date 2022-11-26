package io.flybird.cubecraft.world;

import io.flybird.cubecraft.GameSetting;

public interface IWorldProvider {
    ClientWorld createClientWorld(Level level);
    ServerWorld createServerWorld(Level level, GameSetting setting);
}