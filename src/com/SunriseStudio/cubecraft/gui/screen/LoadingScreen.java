package com.SunriseStudio.cubecraft.gui.screen;

import com.SunriseStudio.cubecraft.gui.DisplayScreenInfo;
import com.SunriseStudio.cubecraft.GameSetting;
import com.SunriseStudio.cubecraft.gui.LoadingScreenTask;
import com.SunriseStudio.cubecraft.gui.component.Button;
import com.SunriseStudio.cubecraft.gui.component.ProgressBar;
import com.SunriseStudio.cubecraft.resources.textures.Texture2D;
import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.gui.layout.OriginLayout;
import org.lwjglx.opengl.Display;

public class LoadingScreen extends Screen{
    private final Texture2D bg;
    public LoadingScreenTask task;
    public ProgressBar progressBar;
    public Screen onFinished;
    public Screen onCancelled;
    public String status;
    //public Label label;

    public LoadingScreen(Screen onCancelled,Screen onFinished,LoadingScreenTask task){
        this.onFinished=onFinished;
        this.onCancelled=onCancelled;
        this.task=task;

        bg=new Texture2D(false,false);
        bg.generateTexture();
        bg.load("/resource/textures/gui/bg.png");
    }

    @Override
    public void init() {
        this.progressBar=new ProgressBar();
        this.progressBar.setLayout(new OriginLayout(0,0,160,16,OriginLayout.Origin.MIDDLE_MIDDLE,0));
        this.components.add(this.progressBar);
        //this.label=new Label("fuck!",26,0xFFFFFF,0, FontRenderer.Alignment.MIDDLE);
        //this.label.setLayout(new OriginLayout(0,-40,0,16, OriginLayout.Origin.MIDDLE_MIDDLE,0));
        //this.components.add(this.label);
        Button button=new Button(0xFFFFFF,0xFFFFFF,"返回");
        button.setLayout(new OriginLayout(0,24,300,24, OriginLayout.Origin.MIDDLE_MIDDLE,1));
        button.setListener(() -> LoadingScreen.this.task.cancel());
        this.components.add(button);
        new Thread(this.task).start();
    }

    @Override
    public void tick() {
        super.tick();
        this.progressBar.setStatus(this.task.getStatus());
        //this.label.text=this.task.getText();
        if(this.task.getStatus()>=100){
            this.cubeCraft.setScreen(this.onFinished);
        }
        if(this.task.isCancelled()){
            this.cubeCraft.setScreen(this.onCancelled);
        }
    }

    public void setTask(LoadingScreenTask task){
        this.task=task;
    }

    @Override
    public void render(DisplayScreenInfo info) {
        this.bg.bind();
        ShapeRenderer.begin();
        ShapeRenderer.drawRectUV(0, Display.getWidth()/ GameSetting.instance.GUIScale,0,Display.getHeight()/GameSetting.instance.GUIScale,-1,-1,0,1,0,1);
        ShapeRenderer.end();
        this.bg.unbind();
        super.render(info);
    }
}
