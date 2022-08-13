package com.sunrisestudio.grass3d.render.culling;
import com.sunrisestudio.grass3d.render.Camera;
import com.sunrisestudio.util.math.AABB;
import org.lwjgl.opengl.*;

public class OcclusionCuller extends ICuller{
    public OcclusionCuller(Camera camera) {
        super(camera);
    }

    public boolean listVisible(int list){
        //关闭颜色写入
        GL11.glColorMask(false,false,false,false);
        GL11.glDepthMask(false);
        //查询像素通过数量
        int queryID=GL15.glGenQueries();
        GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED,queryID);
        GL11.glCallList(list);
        GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
        //重新打开颜色写入
        GL11.glColorMask(true,true,true, true);
        GL11.glDepthMask(true);
        int samples=GL15.glGetQueryObjecti(queryID,GL15.GL_SAMPLES_PASSED);
        GL15.glDeleteQueries(samples);
        return samples>0;
    }

    @Override
    public boolean[] listsVisible(int base, int range) {
        boolean[] booleans=new boolean[range];
        for (int i=base;i<base+range;i++){
            booleans[i-base]=this.listVisible(i);
        }
        return booleans;
    }

    @Override
    public boolean[] listsVisible(int[] list) {
        boolean[] booleans=new boolean[list.length];
        for (int i=0;i< list.length;i++){
            booleans[i]=this.listVisible(list[i]);
        }
        return booleans;
    }


    // ---- unsupported ----
    @Override
    public boolean aabbVisible(AABB aabb) {
        return true;
    }

    @Override
    public boolean[] aabbsVisible(AABB[] aabb) {
        boolean[] booleans=new boolean[aabb.length];
        for (AABB value : aabb) {
            aabbVisible(value);
        }
        return booleans;
    }
}
