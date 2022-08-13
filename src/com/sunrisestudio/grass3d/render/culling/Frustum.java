package com.sunrisestudio.grass3d.render.culling;

import com.sunrisestudio.util.math.AABB;
import org.joml.Vector3d;

//liner math f u
public class Frustum {
    public enum TestResult{
        OUT,
        INSIDE,
        INTERSECT
    }


    TestResult axisAlignedBBInfrustum(double x0,double y0,double z0,double x1,double y1,double z1) {

        Vector3d[] vertices = new Vector3d[0];
        int iTotalIn = 0;
        //获得所有顶点

        vertices[0]=new Vector3d(x0,y0,z0);
        vertices[1]=new Vector3d(x1,y0,z0);
        vertices[2]=new Vector3d(x0,y0,z1);
        vertices[3]=new Vector3d(x1,y0,z1);
        vertices[4]=new Vector3d(x0,y1,z0);
        vertices[5]=new Vector3d(x1,y1,z0);
        vertices[6]=new Vector3d(x0,y1,z1);
        vertices[7]=new Vector3d(x1,y1,z1);

        //测试6个面的8个顶点
        //如果所有点都在一个的背后,那就是外离
        //如果所有点都在每一个面的正面,就是内含
        for(int p = 0; p < 6; ++p) {
            int iInCount = 8;
            int iPtIn = 1;
            for(int i = 0; i < 8; ++i) {
                //测试这个点
               // if(m_plane[p].SideOfPlane(vertices[i]))
                {
                    iPtIn = 0;
                    --iInCount;
                }
            }
            //所有点都在p面背后吗?
            if(iInCount == 0)
                return(TestResult.OUT); //外离
            iTotalIn += iPtIn;
        }
        //如果iTotalIn是6,那么就都是在正面
        if(iTotalIn == 6)
            return(TestResult.INSIDE); //内含
        return(TestResult.INTERSECT); //相交
    }
}
