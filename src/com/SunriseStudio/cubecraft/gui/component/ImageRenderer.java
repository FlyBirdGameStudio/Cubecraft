package com.SunriseStudio.cubecraft.gui.component;


import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.resources.textures.TextureManagerDirect;

public class ImageRenderer extends Component {
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
    }

    @Override
    public void render() {
        TextureManagerDirect.bind(this.file);
        TextureManagerDirect.ImageData imageData= TextureManagerDirect.getData(this.file);
        float u0=0,u1=0,v0=0,v1=0;
        switch (this.vClip){
            case UP -> {
                v0=0;
                v1=this.layoutManager.height*1.0f/imageData.height;
            }
            case MIDDLE -> {
                v0=(imageData.height/2.0f-this.layoutManager.height/2.0f)/imageData.height;
                v1=(imageData.height/2.0f+this.layoutManager.height/2.0f)/imageData.height;
            }
            case DOWN -> {
                v0=(imageData.height/2.0f-this.layoutManager.height/2.0f)/imageData.height;
                v1= 1;
            }
        }
        switch (this.hClip){
            case LEFT -> {
                u0=0;
                u1=(this.layoutManager.width*1.0f/imageData.width)/imageData.width;
            }
            case MIDDLE -> {
                u0=(imageData.width/2.0f-this.layoutManager.width/2.0f)/imageData.width;
                u1=(imageData.width/2.0f+this.layoutManager.width/2.0f)/imageData.width;
            }
            case RIGHT -> {
                u0=(imageData.width/2.0f-this.layoutManager.width/2.0f)/imageData.width;
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
