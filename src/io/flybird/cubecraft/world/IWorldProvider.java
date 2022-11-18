package io.flybird.cubecraft.world;

public interface IWorldProvider {
    ClientWorld createClientWorld(Level level);

    ServerWorld createServerWorld(Level level, String name);
}
