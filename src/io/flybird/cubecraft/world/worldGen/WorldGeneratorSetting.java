package io.flybird.cubecraft.world.worldGen;

import java.util.HashMap;

public record WorldGeneratorSetting(long seed,HashMap<String,Object> map) {

    public double getValueOrDefaultAsDouble(String s, double v) {
        return (double) map.getOrDefault(s,v);
    }

    public long getValueOrDefaultAsLong(String s, long v) {
        return (long) map.getOrDefault(s,v);
    }
}
