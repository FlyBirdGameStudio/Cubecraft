package com.SunriseStudio.cubecraft.world.block;

import com.SunriseStudio.cubecraft.world.Level;

import java.util.Random;

public class CalmLiquidTile
extends LiquidTile {
    protected CalmLiquidTile(int id, int liquidType) {
        super(id, liquidType);
        this.tileId = id - 1;
        this.calmTileId = id;
        this.setTicking(false);
    }

    @Override
    public void tick(Level world, int x, int y, int z, Random random) {
    }

    @Override
    public void neighborChanged(Level world, int x, int y, int z, int type) {
        boolean hasAirNeighbor = false;
        if (world.getTile(x - 1, y, z) == 0) {
            hasAirNeighbor = true;
        }
        if (world.getTile(x + 1, y, z) == 0) {
            hasAirNeighbor = true;
        }
        if (world.getTile(x, y, z - 1) == 0) {
            hasAirNeighbor = true;
        }
        if (world.getTile(x, y, z + 1) == 0) {
            hasAirNeighbor = true;
        }
        if (world.getTile(x, y - 1, z) == 0) {
            hasAirNeighbor = true;
        }
        if (hasAirNeighbor) {
            world.setTileNoUpdate(x, y, z, this.tileId);
        }
        if (this.liquidType == 1 && type == lava.id) {
            world.setTileNoUpdate(x, y, z, rock.id);
        }
        if (this.liquidType == 2 && type == water.id) {
            world.setTileNoUpdate(x, y, z, rock.id);
        }
    }
}
