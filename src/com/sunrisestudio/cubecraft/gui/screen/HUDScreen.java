package com.sunrisestudio.cubecraft.gui.screen;

import com.sunrisestudio.cubecraft.gui.DisplayScreenInfo;
import com.sunrisestudio.cubecraft.GameSetting;
import com.sunrisestudio.cubecraft.CubeCraft;
import com.sunrisestudio.cubecraft.gui.FontAlignment;
import com.sunrisestudio.grass3d.platform.Display;
import com.sunrisestudio.grass3d.platform.Keyboard;
import com.sunrisestudio.grass3d.platform.Mouse;
import com.sunrisestudio.grass3d.render.GLUtil;
import com.sunrisestudio.util.SystemInfo;
import com.sunrisestudio.grass3d.render.ShapeRenderer;
import com.sunrisestudio.cubecraft.render.renderer.ChunkRenderer;
import com.sunrisestudio.cubecraft.gui.FontRenderer;
import com.sunrisestudio.grass3d.render.textures.Texture2D;
import com.sunrisestudio.grass3d.platform.input.InputHandler;
import com.sunrisestudio.grass3d.platform.input.KeyboardCallback;
import com.sunrisestudio.grass3d.platform.input.MouseCallBack;


import java.text.DecimalFormat;

public class HUDScreen extends Screen {
    private Texture2D actionBar=new Texture2D(false,false);


    public HUDScreen(){
        super();
        this.actionBar.generateTexture();
        this.actionBar.load("/resource/textures/gui/actionbar.png");
    }

    @Override
    public void init() {
        Mouse.setGrabbed(true);
    }

    private boolean debugScreen;
    @Override
    public void render(DisplayScreenInfo info) {
        GLUtil.enableBlend();
        this.platform.controller.tick();
        DecimalFormat format=new DecimalFormat();
        format.applyPattern("0.000");
        this.platform._player.turn(Mouse.getDX(),Mouse.getDY());
        if (this.debugScreen) {
            FontRenderer.renderShadow("CubeCraft-" + CubeCraft.VERSION,
                    2, 2, 16777215, 8, FontAlignment.LEFT);

            FontRenderer.renderShadow("帧率:" + this.platform.getShortTickTPS(),
                    2, 12, 16777215, 8, FontAlignment.LEFT);

            FontRenderer.renderShadow("显示:" + Display.getWidth() + "/" + Display.getHeight(),
                    2, 22, 16777215, 8 , FontAlignment.LEFT);

            ChunkRenderer cr= (ChunkRenderer) this.platform.worldRenderer.renderers.get("cubecraft:chunk_renderer");
            FontRenderer.renderShadow("地形渲染(总数/可见/更新):"+cr.allCount+"/"+cr.visibleCount+"/"+cr.updateCount,
                    2,32,16777215,8, FontAlignment.LEFT);

            FontRenderer.renderShadow("位置（x/y/z）:" + format.format(this.platform._player.x) + "/" + format.format(this.platform._player.y) + "/" + format.format(this.platform._player.z),
                    2, 50, 16777215, 8, FontAlignment.LEFT);

            FontRenderer.renderShadow("相机角度（yaw/pitch/roll）:"+ format.format(this.platform._player.xRot) + "/" + format.format(this.platform._player.yRot) + "/" + format.format(this.platform._player.zRot),
                    2,60,0xFFFFFF,8, FontAlignment.LEFT);

            FontRenderer.renderShadow("系统：" + SystemInfo.getOSName()+"/"+SystemInfo.getOSVersion(),
                    info.scrWidth()-2, 2, 16777215, 8, FontAlignment.RIGHT);

            FontRenderer.renderShadow("内存(已使用/总量)："+ SystemInfo.getUsedMemory()+"/"+SystemInfo.getTotalMemory(),
                    info.scrWidth()-2,12,0xFFFFFF,8, FontAlignment.RIGHT);

            FontRenderer.renderShadow("JVM："+ SystemInfo.getJavaName()+"/"+SystemInfo.getJavaVersion(),
                    info.scrWidth()-2,22,0xFFFFFF,8, FontAlignment.RIGHT);


            FontRenderer.renderShadow("位置（x/y/z）:" + format.format(this.platform.player.x) + "/" + format.format(this.platform.player.y) + "/" + format.format(this.platform.player.z),
                    2, 70, 16777215, 8 , FontAlignment.LEFT);
        }


        this.actionBar.bind();
        int w=Display.getWidth()/GameSetting.instance.GUIScale,h= Display.getHeight()/ GameSetting.instance.GUIScale;
        ShapeRenderer.drawRectUV(w/2d-91,w/2d+91, h-22,h,0,0,0,182/256f,0,22/32f);
        int slotBase= (int) (w/2d+20*(-4.5+ platform._player.selectSlot));
        ShapeRenderer.drawRectUV((slotBase)-2, (slotBase+23), h-(23), h,0,0,232/256f,1,0,22/32f);
        GLUtil.disableBlend();
    }

    @Override
    public boolean isInGameGUI() {
        return true;
    }

    @Override
    public MouseCallBack getMouseCallback() {
        return new MouseCallBack(){
            @Override
            public void onScroll(int value) {
                int i=value;
                if (i > 0) {
                   i = 1;
                }
                if (i < 0) {
                   i = -1;
                }

                HUDScreen.this.platform._player.selectSlot+=i;
                if (HUDScreen.this.platform._player.selectSlot>8){
                    HUDScreen.this.platform._player.selectSlot=0;
                }
                if (HUDScreen.this.platform._player.selectSlot<0){
                    HUDScreen.this.platform._player.selectSlot=8;
                }
            }

            @Override
            public void onLeftClick() {
                if(Display.isActive()){
                    Mouse.setGrabbed(true);
                }
            }
        };
    }

    @Override
    public KeyboardCallback getKeyboardCallback() {
        return new KeyboardCallback(){
            @Override
            public void onKeyEventNext() {
                HUDScreen.this.platform.controller.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                HUDScreen.this.platform._player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                if(InputHandler.isDoubleClicked(Keyboard.KEY_SPACE,250)){
                    HUDScreen.this.platform._player.flyingMode=!HUDScreen.this.platform._player.flyingMode;
                }
            }

            @Override
            public void onKeyEventPressed() {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    Mouse.setGrabbed(false);
                    HUDScreen.this.platform._player.releaseAllKeys();
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_LCONTROL) {
                    HUDScreen.this.platform._player.runningMode=!HUDScreen.this.platform._player.runningMode;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
                    HUDScreen.this.debugScreen=!HUDScreen.this.debugScreen;
                }
            }
        };
    }

    @Override
    public Screen getParentScreen() {
        return new PauseScreen();
    }
}
