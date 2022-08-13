package com.sunrisestudio.grass3d.render.draw;

public abstract class IVertexArrayUploader {
    public abstract void upload(IVertexArrayBuilder builder);

    public static IVertexArrayUploader createNewPointedUploader(){return new VertexPointerUploader();}




}
