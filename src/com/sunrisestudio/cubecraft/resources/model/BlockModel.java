package com.sunrisestudio.cubecraft.resources.model;

import com.sunrisestudio.cubecraft.render.character.Cube;
import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;
import com.sunrisestudio.cubecraft.world.block.BlockFacing;

public record BlockModel(int rotation, Cube[] objects) {
    /**
     * @param builder vertex array builder
     * @param rx render at x
     * @param ry render at y
     * @param rz render at z
     * @param wx culling at x
     * @param wy culling at y
     * @param wz culling at z
     */
    public void render(IVertexArrayBuilder builder, long rx, long ry, long rz, long wx, long wy, long wz, BlockFacing facing){

    }
}
