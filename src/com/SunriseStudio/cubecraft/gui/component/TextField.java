package com.SunriseStudio.cubecraft.gui.component;


import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.gui.FontRenderer;
import com.SunriseStudio.cubecraft.resources.textures.TextureManagerDirect;

public class TextField extends Component {
    private static final int HEIGHT=32;
    private String hintString;

    public TextField(String hint) {
        this.hintString=hint;
    }


    @Override
    public void render() {
        drawBack(0,0,layer,0xDDDDDD);
        drawBack(-2,-2,layer+0.1,0x888888);
        FontRenderer.render(this.hintString,this.layoutManager.ax+6,this.layoutManager.ay+6,0x000000,20, FontRenderer.Alignment.LEFT);
    }
    private void drawBack(double rx,double ry,double rz,int col){
        double ax=this.layoutManager.ax+rx;
        double ay=this.layoutManager.ay+ry;
        ShapeRenderer.setColor(col);
        ShapeRenderer.begin2();
        TextureManagerDirect.bind("/fchat/textures/gui/components/text_field/text_field_left.png");
        ShapeRenderer.drawRectUV(ax,ax+HEIGHT,ay,ay+HEIGHT,rz,rz,0,1,0,1);
        TextureManagerDirect.bind("/fchat/textures/gui/components/text_field/text_field_center.png");
        TextureManagerDirect.ImageData data= TextureManagerDirect.getData("/fchat/textures/gui/components/text_field/text_field_center.png");
        ShapeRenderer.drawRectUV(ax+HEIGHT,ax+this.layoutManager.width-HEIGHT,ay,ay+HEIGHT,rz,rz,0, layoutManager.width/(float)data.width,0,1);
        TextureManagerDirect.bind("/fchat/textures/gui/components/text_field/text_field_right.png");
        ShapeRenderer.drawRectUV(ax+this.layoutManager.width-HEIGHT,ax+this.layoutManager.width,ay,ay+HEIGHT,rz,rz,0,1,0,1);
        ShapeRenderer.end();
    }
    

}
