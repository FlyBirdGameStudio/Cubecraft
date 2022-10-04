package com.flybirdstudio.cubecraft.client.render;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.render.model.object.Vertex;
import com.flybirdstudio.cubecraft.world.IWorld;
import org.joml.Vector3d;


public class BlockRenderUtil {
    public static final double CLASSIC_LIGHT_1=1;
    public static final double CLASSIC_LIGHT_2=0.8;
    public static final double CLASSIC_LIGHT_3=0.6;

    public static double getSmoothedLight(IWorld world, long x, long y, long z, Vector3d relativePos){
        return 1;
        //todo:smooth light
    }

    public static Vertex bakeVertex(Vertex v,Vector3d pos,IWorld w,long x,long y,long z,int face){
        if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_smooth_lighting", true)){
            v.multiplyColor(BlockRenderUtil.getSmoothedLight(w,x,y,z,pos));
        }else{
            v.multiplyColor(w.getLight(x,y+1,z)/128d);
        }
        if(GameSetting.instance.getValueAsBoolean("client.render.terrain.use_smooth_lighting", true)){
            v.multiplyColor(getClassicLight(face));
        }
        return v;
    }

    public static double getClassicLight(int face){
        return switch (face) {
            case 0, 1 -> CLASSIC_LIGHT_1;
            case 2, 3 -> CLASSIC_LIGHT_2;
            case 4, 5 -> CLASSIC_LIGHT_3;
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };
    }
}
