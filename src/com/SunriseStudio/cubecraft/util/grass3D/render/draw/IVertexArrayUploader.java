package com.SunriseStudio.cubecraft.util.grass3D.render.draw;

public abstract class IVertexArrayUploader {
    public abstract void upload(IVertexArrayBuilder builder);

    public static IVertexArrayUploader createNewPointedUploader(){return new PointedVertexArrayUploader();}

    public static IVertexArrayUploader createNewBufferedUploader(){return new BufferedVertexArrayUploader();}


}
