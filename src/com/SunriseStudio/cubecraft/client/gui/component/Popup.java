package com.sunrisestudio.cubecraft.client.gui.component;

import com.sunrisestudio.cubecraft.client.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.client.gui.FontAlignment;
import com.sunrisestudio.cubecraft.client.gui.FontRenderer;
import com.sunrisestudio.cubecraft.registery.Registry;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class Popup {
    private int xo;
    private int x;
    public int remaining;

    private final int time;
    private final String logoPath;
    private final String title;
    private final String subTitle;

    public int type;

    public static final int INFO=0;
    public static final int SUCCESS=1;
    public static final int ERROR=2;
    public static final int WARNING=3;


    public Popup(String logoPath, String title, String subTitle,int time,int type) {
        this.logoPath = logoPath;
        this.title = title;
        this.subTitle = subTitle;
        this.time=time;
        this.remaining=time;
        this.type =type;
    }

    public void render(DisplayScreenInfo info){
        GLUtil.enableBlend();
        Registry.getTextureManager().bind2dTexture("/resource/textures/gui/popup.png");
        ShapeRenderer.drawRectUV(4,196,4,46,0,0, 0,1,
                type*42/198f,(type+1)*42/198f
        );

        ShapeRenderer.drawRectUV(8,38,8,38,0,0,
                type*30/198f,(type+1)*30/198f
                ,168/198f,1
        );
        Registry.getTextureManager().unBind2dTexture("/resource/textures/gui/popup.png");
        FontRenderer.renderShadow(title,40,12,0xffffff,12, FontAlignment.LEFT);
        FontRenderer.renderShadow(subTitle,40,28,0xffffff,8, FontAlignment.LEFT);
    }

    public int getTime() {
        return time;
    }

    public void tick() {
        remaining-=1;
        xo=x;
        if(getTime()-remaining<15){
            float i=15-(getTime()-remaining);
            x= (int) (i*i*13);
        }
        if(remaining<15){
            float i=15-remaining;
            x= (int) (i*i*13);
        }
    }

    public int getPos(float t){
        return (int) MathHelper.linear_interpolate(xo,x,t);
    }
}