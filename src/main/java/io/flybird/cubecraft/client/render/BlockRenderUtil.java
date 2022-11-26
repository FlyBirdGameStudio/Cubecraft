package io.flybird.cubecraft.client.render;

import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.render.model.object.Vertex;
import io.flybird.cubecraft.register.Registry;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.util.math.MathHelper;
import io.flybird.util.math.Vector3;
import org.joml.Vector3d;


public class BlockRenderUtil {
    public static final double CLASSIC_LIGHT_1 = 1;
    public static final double CLASSIC_LIGHT_2 = 0.8;
    public static final double CLASSIC_LIGHT_3 = 0.6;
    public static final double CLASSIC_LIGHT_4 = 0.5;

    public static double getSmoothedLight(IWorld world, long x, long y, long z, Vector3d relativePos) {
        double _000 = MathHelper.median(world.getLight(x - 1, y, z), world.getLight(x, y - 1, z), world.getLight(x, y, z - 1));
        double _100 = MathHelper.median(world.getLight(x + 1, y, z), world.getLight(x, y - 1, z), world.getLight(x, y, z - 1));
        double _010 = MathHelper.median(world.getLight(x - 1, y, z), world.getLight(x, y + 1, z), world.getLight(x, y, z - 1));
        double _110 = MathHelper.median(world.getLight(x + 1, y, z), world.getLight(x, y + 1, z), world.getLight(x, y, z - 1));

        double _001 = MathHelper.median(world.getLight(x - 1, y, z), world.getLight(x, y - 1, z), world.getLight(x, y, z + 1));
        double _101 = MathHelper.median(world.getLight(x + 1, y, z), world.getLight(x, y - 1, z), world.getLight(x, y, z + 1));
        double _011 = MathHelper.median(world.getLight(x - 1, y, z), world.getLight(x, y + 1, z), world.getLight(x, y, z + 1));
        double _111 = MathHelper.median(world.getLight(x + 1, y, z), world.getLight(x, y + 1, z), world.getLight(x, y, z + 1));

        return MathHelper.linear_interpolate3d(_000, _001, _010, _011, _100, _101, _110, _111, relativePos.x, relativePos.y, relativePos.z);
        //_000,_001,_010,_011,_100,_101,_110,_111
        //todo:smooth light
    }

    public static Vertex bakeVertex(Vertex v, Vector3d pos, IWorld w, long x, long y, long z, int face) {
        if (Registry.getClient().getGameSetting().getValueAsBoolean("client.render.terrain.use_smooth_lighting", true)) {
            v.multiplyColor(BlockRenderUtil.getSmoothedLight(w, x, y, z, pos) / 128d);
        } else {
            Vector3<Long> v2 = EnumFacing.fromId(face).findNear(x, y, z, 1);
            v.multiplyColor(w.getLight(v2.x(), v2.y(), v2.z()) / 128d);
        }
        if (Registry.getClient().getGameSetting().getValueAsBoolean("client.render.terrain.use_classic_lighting", false)) {
            v.multiplyColor(getClassicLight(face));
        }
        return v;
    }

    public static double getClassicLight(int face) {
        return switch (face) {
            case 0 -> CLASSIC_LIGHT_1;
            case 1-> CLASSIC_LIGHT_4;
            case 2, 3 -> CLASSIC_LIGHT_2;
            case 4, 5 -> CLASSIC_LIGHT_3;
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };
    }

}
