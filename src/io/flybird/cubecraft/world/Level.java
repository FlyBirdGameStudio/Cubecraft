package io.flybird.cubecraft.world;

import io.flybird.cubecraft.register.Registry;
import io.flybird.util.file.FileUtil;

import java.util.HashSet;

public class Level {
    private final String name;
    public HashSet<IWorld> dims = new HashSet<>();
    private LevelInfo levelInfo;

    public Level(String name) {
        for (String s : Registry.getWorldIdList()) {
            dims.add(new ServerWorld(s, levelInfo));
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public long getSeed() {
        return 0;
    }

    public void createLevelFolder() {
        FileUtil.createFileRelative("/data/saves/%s/level.nbt".formatted(this.name));
        for (IWorld world : this.dims) {
            FileUtil.createFileRelative("/data/saves/%s/%s/chunk.ldb".formatted(this.name, world.getID().replace(":","_")));
            FileUtil.createFileRelative("/data/saves/%s/%s/entity.ldb".formatted(this.name, world.getID().replace(":","_")));
        }
    }
}
