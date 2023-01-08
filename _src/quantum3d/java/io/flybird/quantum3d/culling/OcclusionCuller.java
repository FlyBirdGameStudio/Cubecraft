package io.flybird.quantum3d.culling;
import io.flybird.quantum3d.BufferAllocation;
import io.flybird.quantum3d.Camera;
import io.flybird.quantum3d.GLUtil;
import io.flybird.quantum3d.ShapeRenderer;
import io.flybird.quantum3d.draw.VertexArrayBuilder;
import io.flybird.quantum3d.draw.VertexArrayUploader;
import io.flybird.quantum3d.drawcall.IRenderCall;
import io.flybird.quantum3d.drawcall.ListRenderCall;
import io.flybird.util.math.AABB;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class OcclusionCuller extends ICuller{
    private final IntBuffer buffer= BufferAllocation.allocIntBuffer(1);

    public OcclusionCuller(Camera camera) {
        super(camera);
    }

    public boolean listVisible(IRenderCall list){
        GL11.glColorMask(false,false,false,false);
        GL11.glDepthMask(false);
        boolean b=_listVisible(list);
        GL11.glColorMask(true,true,true, true);
        GL11.glDepthMask(true);
        return b;
    }

    public boolean _listVisible(IRenderCall list){
        GLUtil.checkGLError("occlusion:pre_check");
        //关闭颜色写入
        //查询像素通过数量
        int queryID=GL15.glGenQueries();
        GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED,queryID);
        list.call();
        GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
        //重新打开颜色写入
        buffer.clear();
        GL15.glGetQueryObjectiv(queryID,GL15.GL_QUERY_RESULT,buffer);
        int samples=buffer.get(0);
        GL15.glDeleteQueries(queryID);
        return samples>0;
    }

    @Override
    public boolean[] listsVisible(IRenderCall[] calls){
        GL11.glColorMask(false,false,false,false);
        GL11.glDepthMask(false);
        boolean[] b=new boolean[calls.length];
        int i=0;
        for (IRenderCall call:calls){
            //查询像素通过数量
            int queryID=GL15.glGenQueries();
            GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED,queryID);
            call.call();
            GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
            //重新打开颜色写入
            IntBuffer buf= MemoryUtil.memAllocInt(1);
            GL15.glGetQueryObjectiv(queryID,GL15.GL_QUERY_RESULT,buf);
            int samples=buf.get(0);
            GL15.glDeleteQueries(queryID);
            MemoryUtil.memFree(buf);
            b[i]=samples>0;
            i++;
        }
        //关闭颜色写入
        GL11.glColorMask(true,true,true, true);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        return b;
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
        boolean b=this.listVisible(new ListRenderCall(i));
        GL11.glDeleteLists(i,1);
        return b;
    }
}
