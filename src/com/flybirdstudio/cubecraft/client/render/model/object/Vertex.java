package com.flybirdstudio.cubecraft.client.render.model.object;

import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector4d;

public record Vertex(
        Vector3d pos,
        Vector2d texture,
        Vector4d color,
        Vector3d normal
) {


    public Vertex multiplyColor(Vector4d col){
        this.color.mul(col);
        return this;
    }

    public Vertex multiplyColor(double col){
        multiplyColor(new Vector3d(col,col,col));
        return this;
    }

    public Vertex multiplyColor(Vector3d col){
        this.color.mul(new Vector4d(col.x,col.y,col.z,1d));
        return this;
    }


    public void draw(VertexArrayBuilder builder){
        builder.tex(texture);
        builder.color(color);
        builder.normal(normal);
        builder.vertex(pos);
    }

    public static Vertex create(Vector3d pos,Vector2d tex,Vector4d color){
        return new Vertex(pos,tex,color,new Vector3d(1));
    }

    public static Vertex create(Vector3d pos,Vector2d tex){
        return new Vertex(pos,tex,new Vector4d(1),new Vector3d(1));
    }

    public static Vertex create(Vector3d pos,Vector4d color){
        return new Vertex(pos,new Vector2d(0),color,new Vector3d(1));
    }

    public static Vertex create(Vector3d pos, Vector2d tex, Vector3d color) {
        return create(pos,tex,new Vector4d(color,1));
    }
}
