package com.flybirdstudio.cubecraft.client.gui.component;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.gui.layout.LayoutManager;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.cubecraft.client.gui.layout.Border;
import com.flybirdstudio.starfish3d.platform.Display;


public abstract class Component {
    public Screen parent;


    public LayoutManager layoutManager;
    public boolean[] scaleEnabled={false,false,false,false};
    int layer;
    //layout

    public void resize(int width,int height){
        int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
        this.layoutManager.resize(width,height);
        this.layer=layoutManager.layer;
        if(scaleEnabled[0]){//left
            this.layoutManager.ax=0;
        }
        if(scaleEnabled[2]){//top
            this.layoutManager.ay=0;
        }
        if(scaleEnabled[3]){//bottom
            this.layoutManager.aHeight= Display.getHeight()/ scale -this.layoutManager.ay;
        }
        if(scaleEnabled[1]){//right
            this.layoutManager.aWidth= Display.getWidth()/scale-this.layoutManager.ax;
        }
    }

    public void tick(int xm,int ym){}
    public abstract void render();

    public void setLayout(LayoutManager layoutManager){
        this.layoutManager=layoutManager;
    }

    public void setScaleEnabled(boolean l,boolean r,boolean t,boolean b){
        this.scaleEnabled[0]=l;
        this.scaleEnabled[1]=r;
        this.scaleEnabled[2]=b;
        this.scaleEnabled[3]=t;
    }

    public void setBorder(Border border) {
        this.layoutManager.border=border;
    }

    public void onClicked(int xm,int ym) {}
}
