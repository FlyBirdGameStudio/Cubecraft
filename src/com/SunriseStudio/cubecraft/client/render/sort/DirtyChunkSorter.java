package com.sunrisestudio.cubecraft.client.render.sort;

import com.sunrisestudio.cubecraft.world.entity.Entity;
import com.sunrisestudio.cubecraft.client.render.object.RenderChunk;
import com.sunrisestudio.cubecraft.world.entity.humanoid.Player;

import java.util.Comparator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DirtyChunkSorter
implements Comparator<RenderChunk> {
    private final Entity player;

    public DirtyChunkSorter(Player player) {
        this.player = player;
    }

    @Override
    public int compare(RenderChunk c1, RenderChunk c2) {
        if(c1 == null && c2 == null) {
            return 0;
        }
        if(c1 == null) {
            return -1;
        }
        if(c2 == null) {
            return 1;
        }
        if(c1.distanceTo(player) > c2.distanceTo(player)) {
            return 1;
        }
        if(c2.distanceTo(player) > c1.distanceTo(player)) {
            return -1;
        }
        return 0;
    }
}
