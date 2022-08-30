package com.flybirdstudio.starfish3d.render.draw;

public abstract class IVertexArrayUploader {
    public abstract void upload(VertexArrayBuilder builder);

    public static IVertexArrayUploader createNewPointedUploader(){return new VertexPointerUploader();}




}
