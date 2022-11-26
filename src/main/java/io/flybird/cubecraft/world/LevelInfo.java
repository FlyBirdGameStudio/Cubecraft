package io.flybird.cubecraft.world;

import java.util.Date;

public record LevelInfo(
        String name,
        String creator,
        long seed,
        Date createDate,
        boolean cheat,
        Level level
) {
}