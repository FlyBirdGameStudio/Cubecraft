package com.sunrisestudio.cubecraft.gui.component;

import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;


public class ProgressBar extends Component{
    private static final int HEIGHT = 16;
    private int status;

    private Texture2D texture2D=new Texture2D(false,false);

    public ProgressBar(){
        this.texture2D.generateTexture();
        this.texture2D.load("resource/textures/gui/progress_bar.png");
    }


    @Override
    public void render() {
        this.texture2D.bind();
        int x=this.layoutManager.ax;
        int y=this.layoutManager.ay;
        int w=this.layoutManager.aWidth;
        int h=this.layoutManager.aHeight;
        int z=this.layer;
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(x,x+HEIGHT,y,y+h,z,z,0,0.1,0,1);
        ShapeRenderer.drawRectUV(x+HEIGHT,x+w-HEIGHT,y,y+h,z,z,0.1,0.9,0,1);
        ShapeRenderer.drawRectUV(x+w-HEIGHT,x+w,y,y+h,z,z,0.9,1,0,1);
        ShapeRenderer.end();
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(x+1,x+HEIGHT,y+1,y+h-1,z,z,0,0.1,0,1);
        ShapeRenderer.drawRectUV(x+HEIGHT,x+w-HEIGHT,y,y+h,z,z,0.1,0.9,0,1);
        ShapeRenderer.drawRectUV(x+w-HEIGHT,x+w,y,y+h,z,z,0.9,1,0,1);
        ShapeRenderer.end();

    }

    public void setStatus(int status) {
        this.status = status;
    }
}
