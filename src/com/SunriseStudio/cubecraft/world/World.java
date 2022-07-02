package com.SunriseStudio.cubecraft.world;

import java.util.HashSet;

public class World {
    public HashSet<IDimensionAccess> dims=new HashSet<>();
    private String name;
    private long createTime;

    public World(String name) {
        this.name=name;
        this.createTime=System.currentTimeMillis();
    }

    public String getName() {
        return this.name;
    }
}
