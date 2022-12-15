package io.flybird.starfish3d.render.culling;
import io.flybird.starfish3d.render.Camera;
import io.flybird.starfish3d.render.GLUtil;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.util.math.AABB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.IntBuffer;

public class OcclusionCuller extends ICuller{
    public OcclusionCuller(Camera camera) {
        super(camera);
    }

    public boolean listVisible(int list){
        GLUtil.checkGLError("occlusion:pre_check");
        //关闭颜色写入
        GL11.glColorMask(false,false,false,false);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);
        //查询像素通过数量
        int queryID=GL15.glGenQueries();
        GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED,queryID);
        GL11.glCallList(list);
        GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
        //重新打开颜色写入
        GL11.glColorMask(true,true,true, true);
        GL11.glDepthMask(true);
        IntBuffer buf= BufferUtils.createIntBuffer(1);
        GL15.glGetQueryObjectiv(queryID,GL15.GL_QUERY_RESULT,buf);
        int samples=buf.get(0);
        GL15.glDeleteQueries(queryID);
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

    @Override
    public boolean aabbVisible(AABB aabb) {
        int i=GL11.glGenLists(1);
        GL11.glNewList(i,GL11.GL_COMPILE);
        VertexArrayBuilder builder=new VertexArrayBuilder(24);
        builder.begin();
        ShapeRenderer.renderAABBBox(builder,aabb);
        builder.end();
        VertexArrayUploader.uploadPointer(builder);
        GL11.glEndList();
        boolean b=this.listVisible(i);
        GL11.glDeleteLists(i,1);
        return b;
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
