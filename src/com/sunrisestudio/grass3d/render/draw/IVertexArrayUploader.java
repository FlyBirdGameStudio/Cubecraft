package com.sunrisestudio.grass3d.render.draw;

public abstract class IVertexArrayUploader {
    public abstract void upload(IVertexArrayBuilder builder);

    public static IVertexArrayUploader createNewPointedUploader(){return new PointedVertexArrayUploader();}

    public static IVertexArrayUploader createNewBufferedUploader(){return new BufferedVertexArrayUploader();}


}
