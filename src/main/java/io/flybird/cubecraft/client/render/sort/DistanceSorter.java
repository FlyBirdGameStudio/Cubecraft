package io.flybird.cubecraft.client.render.sort;

import io.flybird.cubecraft.world.entity.humanoid.Player;

import java.util.Comparator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DistanceSorter implements Comparator<DistanceComparable> {
    private Player player;

    public DistanceSorter(Player player) {
        this.player = player;
    }

    @Override
    public int compare(DistanceComparable c1, DistanceComparable c2) {
        if(c1 == null && c2 == null) {
            return 0;
        }
        if(c1 == null) {
            return -1;
        }
        if(c2 == null) {
            return 1;
        }
        return -Double.compare(c2.distanceTo(player), c1.distanceTo(player));
    }
}
