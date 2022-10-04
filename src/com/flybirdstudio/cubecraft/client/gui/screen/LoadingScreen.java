package com.flybirdstudio.cubecraft.client.gui.screen;

import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.client.gui.LoadingScreenTask;
import com.flybirdstudio.cubecraft.client.gui.ScreenUtil;
import com.flybirdstudio.cubecraft.client.gui.component.Button;
import com.flybirdstudio.cubecraft.client.gui.component.ProgressBar;
import com.flybirdstudio.cubecraft.client.gui.layout.OriginLayout;
import com.flybirdstudio.util.file.lang.Language;

public class LoadingScreen extends Screen{
    public LoadingScreenTask task;
    public ProgressBar progressBar;
    public Screen onFinished;
    public Screen onCancelled;
    public String status;
    //public Label label;

    public LoadingScreen(Screen onCancelled,Screen onFinished,LoadingScreenTask task){
        super("cubecraft:loading_screen", ScreenType.IMAGE_BACKGROUND);
        this.onFinished=onFinished;
        this.onCancelled=onCancelled;
        this.task=task;
    }

    @Override
    public void init() {
        this.progressBar=new ProgressBar();
        this.progressBar.setLayout(new OriginLayout(0,0,160,16,OriginLayout.Origin.MIDDLE_MIDDLE,0));
        this.addComponent(this.progressBar);
        //this.label=new Label("fuck!",26,0xFFFFFF,0, FontRenderer.Alignment.MIDDLE);
        //this.label.setLayout(new OriginLayout(0,-40,0,16, OriginLayout.Origin.MIDDLE_MIDDLE,0));
        //this.components.add(this.label);
        Button button=new Button(0xFFFFFF, Language.get("loadingworld"));
        button.setLayout(new OriginLayout(0,24,300,24, OriginLayout.Origin.MIDDLE_MIDDLE,1));
        button.setListener(() -> LoadingScreen.this.task.cancel());
        this.addComponent(button);
        new Thread(this.task).start();
    }

    @Override
    public void tick() {
        super.tick();
        this.progressBar.setStatus(this.task.getStatus());
        //this.label.text=this.task.getText();
        if(this.task.getStatus()>=100){
            this.getPlatform().setScreen(this.onFinished);
        }
        if(this.task.isCancelled()){
            this.getPlatform().setScreen(this.onCancelled);
        }
    }

    public void setTask(LoadingScreenTask task){
        this.task=task;
    }

    @Override
    public void render(DisplayScreenInfo info, float interpolationTime) {
        super.render(info, interpolationTime);
        ScreenUtil.renderPictureBackground();
    }
}
