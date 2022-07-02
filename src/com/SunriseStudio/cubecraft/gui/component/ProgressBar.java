package com.SunriseStudio.cubecraft.gui.component;

import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.resources.textures.TextureManagerDirect;


public class ProgressBar extends Component{
    private static final int HEIGHT = 16;
    private int status;

    @Override
    public void render() {

        int x=this.layoutManager.ax;
        int y=this.layoutManager.ay;
        int w=this.layoutManager.aWidth;
        int h=this.layoutManager.aHeight;
        int z=this.layer;
        TextureManagerDirect.bind("/resources/resource/minecraft/textures/gui/progress_bar.png");
        ShapeRenderer.setColor(0xFFFFFF);
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(x,x+HEIGHT,y,y+h,z,z,0,0.1,0,1);
        ShapeRenderer.drawRectUV(x+HEIGHT,x+w-HEIGHT,y,y+h,z,z,0.1,0.9,0,1);
        ShapeRenderer.drawRectUV(x+w-HEIGHT,x+w,y,y+h,z,z,0.9,1,0,1);
        ShapeRenderer.end();
        TextureManagerDirect.bind("/resources/resource/minecraft/textures/gui/progress_bar.png");
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
