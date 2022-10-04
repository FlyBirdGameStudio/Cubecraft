package com.flybirdstudio.cubecraft.client.gui.component;

import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.gui.layout.Border;
import com.flybirdstudio.cubecraft.client.gui.layout.LayoutManager;
import com.flybirdstudio.cubecraft.client.gui.screen.Screen;
import com.flybirdstudio.starfish3d.platform.Display;


public abstract class Component {
    protected Screen parent;



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

    @Deprecated()//move to layout manager
    public void setScaleEnabled(boolean l,boolean r,boolean t,boolean b){
        this.scaleEnabled[0]=l;
        this.scaleEnabled[1]=r;
        this.scaleEnabled[2]=b;
        this.scaleEnabled[3]=t;
    }

    @Deprecated
    public void setBorder(Border border) {
        this.layoutManager.border=border;
    }

    public void onClicked(int xm,int ym) {}

    public static Class<? extends Component> getClass(String name){
        return switch (name){
            case "button"->Button.class;
            case "label"->Label.class;
            case "image"->ImageRenderer.class;
            case "splash"->SplashText.class;
            default -> throw new IllegalArgumentException("no matched constant named %s".formatted(name));
        };
    }

    public Screen getParent() {
        return parent;
    }

    public void setParent(Screen parent) {
        this.parent = parent;
    }

    public String getID() {
        return this.getParent().getComponents().of(this);
    }
}
