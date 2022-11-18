package io.flybird.cubecraft.client.gui.screen;

import io.flybird.cubecraft.client.gui.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.FontAlignment;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.resources.ResourceLocation;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.render.ShapeRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.starfish3d.render.textures.Texture2D;
import io.flybird.util.JVMInfo;
import org.lwjgl.opengl.GL11;

public class LogoLoadingScreen extends Screen {
    private float prog;
    public static final int BG_COLOR = 0x303030;

    private Texture2D logoTex;
    private String text;
    private float alpha;
    private int animateStatus=0;

    private boolean checked;

    public LogoLoadingScreen() {
        super(false, "cubecraft:logo_loading", ScreenType.IMAGE_BACKGROUND);
    }

    @Override
    public void init() {
        this.logoTex = new Texture2D(false, false);
        this.logoTex.generateTexture();
        logoTex.load(ResourceManager.instance.getResource(ResourceLocation.uiTexture("cubecraft","icons/logo.png")));
    }

    @Override
    public void render(DisplayScreenInfo info, float interpolationTime) {
        if(alpha>0){
            GL11.glPushMatrix();
            GL11.glTranslatef(0,0,20);
            VertexArrayBuilder builder=new VertexArrayBuilder(512);
            int xc = info.centerX();
            int yc = info.centerY();

            //render logo
            this.logoTex.bind();
            builder.begin();
            builder.color(1,1,1,alpha);
            ShapeRenderer.drawRectUV(builder,xc - 160, xc + 160, yc - 50, yc + 30, 1, 1, 0, 1, 0, 1);
            builder.end();
            VertexArrayUploader.uploadPointer(builder);
            this.logoTex.unbind();
            builder.begin();
            if(animateStatus==0){
                Runtime runtime=Runtime.getRuntime();
                this.drawProgressBar(builder,xc, 20,130, 1-runtime.freeMemory()/ (float)runtime.totalMemory());
                ScreenUtil.drawFontASCII("Mem-%s/%s-%s".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage()), xc, 8, 16777215, 8, FontAlignment.MIDDLE);
                ScreenUtil.drawFontASCII("Loading-" + text + "(" + (int) (prog * 100) + "%)", xc - 250, yc + 65, 16777215, 8, FontAlignment.LEFT);
            }

            this.drawProgressBar(builder,xc, yc + 80,250, this.prog);
            ShapeRenderer.drawRect(builder,0,info.scrWidth(),0,info.scrHeight(),-1,-1);
            builder.end();
            VertexArrayUploader.uploadPointer(builder);
            GL11.glPopMatrix();
        }
        if(alpha>-0.3&&animateStatus==-1) {
            alpha -= 0.02;
        }
        if(alpha<1.1&&animateStatus==1) {
            alpha += 0.02;
        }
        if(alpha>=1&&checked){
            this.getPlatform().setScreen(this);
            animateStatus=0;
            checked=false;
        }
    }

    public void updateProgress(float prog) {
        this.prog = prog;
    }

    public void setText(String newStage) {
        this.text = newStage;
    }

    public void drawProgressBar(VertexArrayBuilder builder,int xc, int yc,int halfSize, float prog) {
        //render progress bar
        float x0_out = xc - halfSize, x1_out = xc + halfSize, y0_out = yc, y1_out = yc + 10;
        float x0_in = xc - halfSize+1, x1_in = xc + halfSize-1, y0_in = yc + 1, y1_in = yc + 9;
        float x0_prog = xc - halfSize+2, x1_prog = xc - halfSize + prog * halfSize*2 - 2, y0_prog = yc + 2, y1_prog = yc + 8;

        builder.color(1,1,1,alpha);

        //render outline
        builder.vertex(x0_out, y1_out, 1);
        builder.vertex(x1_out, y1_out, 1);
        builder.vertex(x1_out, y0_out, 1);
        builder.vertex(x0_out, y0_out, 1);

        //render progress
        builder.vertex(x0_prog, y1_prog, 3);
        builder.vertex(x1_prog, y1_prog, 3);
        builder.vertex(x1_prog, y0_prog, 3);
        builder.vertex(x0_prog, y0_prog, 3);

        builder.color(30/255f,30/255f,30/255f,alpha);

        //render inner
        builder.vertex(x0_in, y1_in, 2);
        builder.vertex(x1_in, y1_in, 2);
        builder.vertex(x1_in, y0_in, 2);
        builder.vertex(x0_in, y0_in, 2);
    }

    public void dispose(){
        alpha=1;
        animateStatus=-1;
    }

    public void intro(){
        alpha=0;
        animateStatus=1;
        checked=true;
    }

    public void display(){
        alpha=1;
        animateStatus=0;
    }

}
