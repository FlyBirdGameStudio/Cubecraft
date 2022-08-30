package com.flybirdstudio.cubecraft.world;

import java.util.Date;

public record LevelInfo(
        String name,
        String creator,
        long seed,
        Date createDate,
        boolean cheat,
        String worldType,
        Level level
) {
}