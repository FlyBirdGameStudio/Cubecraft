package com.flybirdstudio.starfish3d.render.draw;

import org.lwjgl.opengl.GL11;

public record VertexFormat(DataFormat v, DataFormat c, DataFormat t, DataFormat n, int type) {
    //v3f
    public static final VertexFormat V3F_C4F_T2F_N3F=new VertexFormat(DataFormat.Float3,DataFormat.Float4,DataFormat.Float2,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V3F_C4F_T3F_N3F=new VertexFormat(DataFormat.Float3,DataFormat.Float4,DataFormat.Float3,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V3F_C3F_T2F_N3F=new VertexFormat(DataFormat.Float3,DataFormat.Float3,DataFormat.Float2,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V3F_C3F_T3F_N3F=new VertexFormat(DataFormat.Float3,DataFormat.Float3,DataFormat.Float3,DataFormat.Float3, GL11.GL_FLOAT);

    //v2f
    public static final VertexFormat V2F_C4F_T2F_N3F=new VertexFormat(DataFormat.Float2,DataFormat.Float4,DataFormat.Float2,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V2F_C4F_T3F_N3F=new VertexFormat(DataFormat.Float2,DataFormat.Float4,DataFormat.Float3,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V2F_C3F_T2F_N3F=new VertexFormat(DataFormat.Float2,DataFormat.Float3,DataFormat.Float2,DataFormat.Float3, GL11.GL_FLOAT);
    public static final VertexFormat V2F_C3F_T3F_N3F=new VertexFormat(DataFormat.Float2,DataFormat.Float3,DataFormat.Float3,DataFormat.Float3, GL11.GL_FLOAT);

    //v3d
    public static final VertexFormat V3D_C4D_T2D_N3D=new VertexFormat(DataFormat.Double3,DataFormat.Double4,DataFormat.Double2,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V3D_C4D_T3D_N3D=new VertexFormat(DataFormat.Double3,DataFormat.Double4,DataFormat.Double3,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V3D_C3D_T2D_N3D=new VertexFormat(DataFormat.Double3,DataFormat.Double3,DataFormat.Double2,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V3D_C3D_T3D_N3D=new VertexFormat(DataFormat.Double3,DataFormat.Double3,DataFormat.Double3,DataFormat.Double3, GL11.GL_DOUBLE);

    //v2d
    public static final VertexFormat V2D_C4D_T2D_N3D=new VertexFormat(DataFormat.Double2,DataFormat.Double4,DataFormat.Double2,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V2D_C4D_T3D_N3D=new VertexFormat(DataFormat.Double2,DataFormat.Double4,DataFormat.Double3,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V2D_C3D_T2D_N3D=new VertexFormat(DataFormat.Double2,DataFormat.Double3,DataFormat.Double2,DataFormat.Double3, GL11.GL_DOUBLE);
    public static final VertexFormat V2D_C3D_T3D_N3D=new VertexFormat(DataFormat.Double2,DataFormat.Double3,DataFormat.Double3,DataFormat.Double3, GL11.GL_DOUBLE);
}
