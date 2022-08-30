package com.flybirdstudio.cubecraft.client.gui.screen;

import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.FontAlignment;
import com.flybirdstudio.starfish3d.render.textures.Texture2D;
import com.flybirdstudio.starfish3d.render.GLUtil;
import com.flybirdstudio.starfish3d.render.ShapeRenderer;
import com.flybirdstudio.starfish3d.render.draw.VertexArrayBuilder;
import com.flybirdstudio.starfish3d.render.draw.IVertexArrayUploader;
import org.lwjgl.opengl.GL11;

public class LogoLoadingScreen extends Screen {
    private float prog;
    public static final int BG_COLOR=0x303030;

    private Texture2D logoTex;
    private String text;

    @Override
    public void init() {
        this.logoTex=new Texture2D(false,false);
        this.logoTex.generateTexture();
        logoTex.load("/resource/textures/gui/logo.png");
    }

    @Override
    public void render(DisplayScreenInfo info,float interpolationTime) {
        GLUtil.enableBlend();

        int xc=info.centerX();
        int yc=info.centerY();

        //render bg
        GL11.glClearColor(40/255f,40/255f,40/255f,1);

        //render logo
        ShapeRenderer.setColor(0xFFFFFF);
        this.logoTex.bind();
        ShapeRenderer.drawRectUV(xc-160,xc+160,yc-50,yc+30,1,1,0,1,0,1);
        this.logoTex.unbind();

        //render progress bar
        VertexArrayBuilder builder=new VertexArrayBuilder(12);
        float x0_out=xc-250,x1_out=xc+250,y0_out=yc+80,y1_out=yc+90;
        float x0_in=xc-249,x1_in=xc+249,y0_in=yc+81,y1_in=yc+89;
        float x0_prog=xc-248,x1_prog=xc-250+prog*500-2,y0_prog=yc+82,y1_prog=yc+88;
        builder.begin();
        builder.color(0xFFFFFF);

        //render outline
        builder.vertex(x0_out,y1_out,1);
        builder.vertex(x1_out,y1_out,1);
        builder.vertex(x1_out,y0_out,1);
        builder.vertex(x0_out,y0_out,1);

        //render progress
        builder.vertex(x0_prog,y1_prog,3);
        builder.vertex(x1_prog,y1_prog,3);
        builder.vertex(x1_prog,y0_prog,3);
        builder.vertex(x0_prog,y0_prog,3);

        builder.color(BG_COLOR);

        //render inner
        builder.vertex(x0_in,y1_in,2);
        builder.vertex(x1_in,y1_in,2);
        builder.vertex(x1_in,y0_in,2);
        builder.vertex(x0_in,y0_in,2);

        builder.end();
        IVertexArrayUploader.createNewPointedUploader().upload(builder);

        Screen.drawFontASCII("Loading-"+text+"("+(int)(prog*100)+"%)", xc-250, yc+65,16777215,8, FontAlignment.LEFT);
    }

    public void updateProgress(float prog) {
        this.prog=prog;
    }

    public void setText(String newStage) {
        this.text=newStage;
    }
}
