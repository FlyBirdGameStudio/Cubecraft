package com.SunriseStudio.cubecraft.gui.screen;

import com.SunriseStudio.cubecraft.gui.DisplayScreenInfo;
import com.SunriseStudio.cubecraft.GameSetting;
import com.SunriseStudio.cubecraft.CubeCraft;
import com.SunriseStudio.cubecraft.util.SystemInfo;
import com.SunriseStudio.cubecraft.util.grass3D.render.ShapeRenderer;
import com.SunriseStudio.cubecraft.render.renderer.ChunkRenderer;
import com.SunriseStudio.cubecraft.gui.FontRenderer;
import com.SunriseStudio.cubecraft.resources.textures.TextureManagerDirect;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;

import java.text.DecimalFormat;

public class HUDScreen extends Screen {
    private boolean debugScreen;
    @Override
    public void render(DisplayScreenInfo info) {
        DecimalFormat format=new DecimalFormat();
        format.applyPattern("#.000");
        this.cubeCraft._player.turn(Mouse.getDX(),Mouse.getDY());
        if (this.debugScreen) {
            FontRenderer.renderShadow("CubeCraft" + CubeCraft.VERSION,
                    2, 2, 16777215, 8, FontRenderer.Alignment.LEFT);

            FontRenderer.renderShadow("帧率:" + this.cubeCraft.getShortTickTPS(),
                    2, 12, 16777215, 8, FontRenderer.Alignment.LEFT);

            FontRenderer.renderShadow("显示:" + Display.getWidth() + "/" + Display.getHeight(),
                    2, 22, 16777215, 8 , FontRenderer.Alignment.LEFT);

            ChunkRenderer cr= (ChunkRenderer) this.cubeCraft.worldRenderer.renderers.get("openMC/renderer:chunk");
            FontRenderer.renderShadow("地形渲染(总数/可见/更新):"+cr.allCount+"/"+cr.visibleCount+"/"+cr.updateCount,
                    2,32,16777215,8, FontRenderer.Alignment.LEFT);

            FontRenderer.renderShadow("位置（x/y/z）:" + format.format(this.cubeCraft._player.x) + "/" + format.format(this.cubeCraft._player.y) + "/" + format.format(this.cubeCraft._player.z),
                    2, 50, 16777215, 8, FontRenderer.Alignment.LEFT);

            FontRenderer.renderShadow("相机角度（yaw/pitch/roll）:"+ format.format(this.cubeCraft._player.xRot) + "/" + format.format(this.cubeCraft._player.yRot) + "/" + format.format(this.cubeCraft._player.zRot),
                    2,60,0xFFFFFF,8, FontRenderer.Alignment.LEFT);

            FontRenderer.renderShadow("系统：" + SystemInfo.getOSName()+"/"+SystemInfo.getOSVersion(),
                    info.scrWidth()-2, 2, 16777215, 8, FontRenderer.Alignment.RIGHT);

            FontRenderer.renderShadow("内存(已使用/总量)："+ SystemInfo.getUsedMemory()+"/"+SystemInfo.getTotalMemory(),
                    info.scrWidth()-2,12,0xFFFFFF,8, FontRenderer.Alignment.RIGHT);

            FontRenderer.renderShadow("JVM："+ SystemInfo.getJavaName()+"/"+SystemInfo.getJavaVersion(),
                    info.scrWidth()-2,22,0xFFFFFF,8, FontRenderer.Alignment.RIGHT);

        }


        TextureManagerDirect.bind("/resources/resource/minecraft/textures/gui/actionbar.png");
        int w=Display.getWidth()/GameSetting.instance.GUIScale,h= Display.getHeight()/ GameSetting.instance.GUIScale;
        ShapeRenderer.drawRectUV(w/2d-91,w/2d+91, h-22,h,0,0,0,1,0,1);
        int slotBase= (int) (w/2d+20*(-4.5+ cubeCraft._player.selectSlot));
        TextureManagerDirect.bind("/resources/resource/minecraft/textures/gui/actionbar_selection.png");
        ShapeRenderer.drawRectUV((slotBase-2),
                (slotBase+22),
                h-(23),
                h+1,0,0,0,1,0,1);
    }

    @Override
    public void init() {

    }


    @Override
    public void tick() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                    Mouse.setGrabbed(false);
                    this.cubeCraft.setScreen(new PauseScreen());
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
                    this.cubeCraft._player.flyingMode=!this.cubeCraft._player.flyingMode;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
                    this.debugScreen=!this.debugScreen;
                }
            }
            this.cubeCraft._player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
        }
        while (Mouse.next()){

        }
    }

    @Override
    public boolean isInGameGUI() {
        return true;
    }
}
