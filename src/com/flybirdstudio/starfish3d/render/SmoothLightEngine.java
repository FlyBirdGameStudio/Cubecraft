package com.flybirdstudio.starfish3d.render;

import com.flybirdstudio.cubecraft.world.IWorld;
import com.flybirdstudio.util.math.MathHelper;
import org.joml.Vector3d;

public class SmoothLightEngine {
    public static double get(IWorld world, long x, long y, long z, Vector3d relativePos){
        int l000= (int) MathHelper.max3(world.getLight(x-1,y,z), world.getLight(x,y-1,z),world.getLight(x,y,z-1));
        int l001= (int) MathHelper.max3(world.getLight(x-1,y,z), world.getLight(x,y-1,z),world.getLight(x,y,z+1));
        int l010= (int) MathHelper.max3(world.getLight(x-1,y,z), world.getLight(x,y+1,z),world.getLight(x,y,z-1));
        int l011= (int) MathHelper.max3(world.getLight(x-1,y,z), world.getLight(x,y+1,z),world.getLight(x,y,z+1));
        int l100= (int) MathHelper.max3(world.getLight(x+1,y,z), world.getLight(x,y-1,z),world.getLight(x,y,z-1));
        int l101= (int) MathHelper.max3(world.getLight(x+1,y,z), world.getLight(x,y-1,z),world.getLight(x,y,z+1));
        int l110= (int) MathHelper.max3(world.getLight(x+1,y,z), world.getLight(x,y+1,z),world.getLight(x,y,z-1));
        int l111= (int) MathHelper.max3(world.getLight(x+1,y,z), world.getLight(x,y+1,z),world.getLight(x,y,z+1));

        int z_000_001= (int) MathHelper.linear_interpolate(l000,l001,relativePos.z);
        int z_100_101= (int) MathHelper.linear_interpolate(l100,l101,relativePos.z);
        int z_010_011= (int) MathHelper.linear_interpolate(l010,l011,relativePos.z);
        int z_110_111= (int) MathHelper.linear_interpolate(l110,l111,relativePos.z);

        int x_10_11=(int)MathHelper.linear_interpolate(z_010_011,z_110_111,relativePos.x);
        int x_00_01=(int)MathHelper.linear_interpolate(z_000_001,z_100_101,relativePos.x);

        //return MathHelper.linear_interpolate(x_00_01,x_10_11,relativePos.y)/127d;
        return 1;
    }
}
