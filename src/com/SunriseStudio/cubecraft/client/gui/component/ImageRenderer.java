package com.sunrisestudio.cubecraft.client.gui.component;


import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;

public class ImageRenderer extends Component {
    private final Texture2D texture=new Texture2D(false,false);
    private String file;

    public enum VerticalClipping{
        UP,
        MIDDLE,
        DOWN
    }
    public enum HorizontalClipping {
        LEFT,
        MIDDLE,
        RIGHT
    }
    public HorizontalClipping hClip;
    public VerticalClipping vClip;
    public ImageRenderer(String file,HorizontalClipping hClip,VerticalClipping vClip) {
        this.hClip=hClip;
        this.vClip=vClip;
        this.file=file;
        this.texture.generateTexture();
        this.texture.load(file);
        this.texture.bind();
    }

    @Override
    public void render() {
        this.texture.bind();
        float u0=0,u1=0,v0=0,v1=0;
        switch (this.vClip){
            case UP -> {
                v0=0;
                v1=this.layoutManager.height*1.0f/this.texture.getHeight();
            }
            case MIDDLE -> {
                v0=(this.texture.getHeight()/2.0f-this.layoutManager.height/2.0f)/this.texture.getHeight();
                v1=(this.texture.getHeight()/2.0f+this.layoutManager.height/2.0f)/this.texture.getHeight();
            }
            case DOWN -> {
                v0=(this.texture.getHeight()/2.0f-this.layoutManager.height/2.0f)/this.texture.getHeight();
                v1= 1;
            }
        }
        switch (this.hClip){
            case LEFT -> {
                u0=0;
                u1=(this.layoutManager.width*1.0f/this.texture.getWidth())/this.texture.getWidth();
            }
            case MIDDLE -> {
                u0=(this.texture.getWidth()/2.0f-this.layoutManager.width/2.0f)/this.texture.getWidth();
                u1=(this.texture.getWidth()/2.0f+this.layoutManager.width/2.0f)/this.texture.getWidth();
            }
            case RIGHT -> {
                u0=(this.texture.getWidth()/2.0f-this.layoutManager.width/2.0f)/this.texture.getWidth();
                u1=1;
            }
        }
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(layoutManager.ax,
                layoutManager.ax+layoutManager.width,
                layoutManager.ay,
                layoutManager.ay+layoutManager.height,
                layer,layer,u0,u1,v0,v1);
        ShapeRenderer.end();
    }
}
