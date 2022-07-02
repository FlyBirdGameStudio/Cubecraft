/*
    Copyright (c) [Year] [name of copyright holder]
    [Software Name] is licensed under Mulan PSL v2.
    You can use this software according to the terms and conditions of the Mulan PSL v2.
    You may obtain a copy of Mulan PSL v2 at:
    http://license.coscl.org.cn/MulanPSL2
    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
    EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
    MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
    See the Mulan PSL v2 for more details.
 */
package com.SunriseStudio.cubecraft;

import com.SunriseStudio.cubecraft.gui.screen.Screen;
import com.SunriseStudio.cubecraft.render.renderer.ContentWorldRenderer;
import com.SunriseStudio.cubecraft.gui.FontRenderer;
import com.SunriseStudio.cubecraft.resources.ResourcePacks;
import com.SunriseStudio.cubecraft.resources.ResourceUtil;
import com.SunriseStudio.cubecraft.util.grass3D.render.GLUtil;
import com.SunriseStudio.cubecraft.util.math.AABB;
import com.SunriseStudio.cubecraft.util.timer.Timer;
import com.SunriseStudio.cubecraft.world.Dimension;
import com.SunriseStudio.cubecraft.world.block.registery.BlockMap;
import com.SunriseStudio.cubecraft.world.entity._Player;
import com.SunriseStudio.cubecraft.world.entity.humanoid.Player;
import com.SunriseStudio.cubecraft.gui.DisplayScreenInfo;
import com.SunriseStudio.cubecraft.gui.screen.HUDScreen;
import com.SunriseStudio.cubecraft.gui.screen.LogoLoadingScreen;
import com.SunriseStudio.cubecraft.gui.screen.TitleScreen;
import com.SunriseStudio.cubecraft.resources.textures.Texture2D;
import com.SunriseStudio.cubecraft.util.LogHandler;
import com.SunriseStudio.cubecraft.util.LoopTickingApplication;
import com.SunriseStudio.cubecraft.world.IDimensionAccess;
import com.SunriseStudio.cubecraft.world.Level;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.DisplayMode;
import org.lwjglx.opengl.PixelFormat;


//todo:add server net support;
//todo:add resource support;
//todo:add inventory support

public class CubeCraft extends LoopTickingApplication {
    private DisplayScreenInfo screenInfo;

    public static final String VERSION = "alpha-0.1.0";
    public Level world;

    public ContentWorldRenderer worldRenderer;
    private Screen screen;
    public _Player _player;

    private final IDimensionAccess clientWorld = new Dimension();
    private final Player player = new Player(clientWorld);

    @Override
    public void init() throws LWJGLException {
        this.timer=new Timer(20);
        this.logHandler = LogHandler.create("main", "client");

        this.initDisplay();
        this.initGameContent();

        this.setScreen(new TitleScreen());

        this.world = new Level(0);
        this._player = new _Player(this.world);
        _player.setPos(1024,140, 1024);
        this.worldRenderer = new ContentWorldRenderer(this.world, this._player);
    }

    @Override
    public void stop() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
        logHandler.info("game stopped...");
        LogHandler.allSave();
        System.exit(0);
    }

    @Override
    public void shortTick() {
        this.logHandler.checkGLError("pre_render");
        float a=this.timer.interpolatedTime;

        this.screenInfo=getDisplaySize();
        GL11.glClear(16640);
        if (this.screen.isInGameGUI()) {
            this.logHandler.checkGLError("pre_world_render");
            worldRenderer.render(a,this.screenInfo.scrWidth(),this.screenInfo.scrHeight());
            this.logHandler.checkGLError("post_world_render");
        }

        if(Display.isCloseRequested()){
            this.stop();
        }

        GLUtil.setupOrthogonalCamera(0, 0, Display.getWidth(),Display.getHeight(),screenInfo.scrWidth(), screenInfo.scrHeight());

        if (this.screen != null) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            this.logHandler.checkGLError("pre_screen_render");
            this.screenInfo=this.getDisplaySize();
            this.screen.render(this.screenInfo);
            this.logHandler.checkGLError("post_screen_render");
        }
        Display.sync(120);
        Display.update();
        this.logHandler.checkGLError("post_render");
    }

    @Override
    public void longTick() {
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                //this.handleMouseClick();
                if (Display.isActive() && this.screen instanceof HUDScreen) {
                    Mouse.setGrabbed(true);
                }
            }
            if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                //right click
            }
        }
        this.screen.tick();
        this.world.tick();
    }



    private boolean isFree(AABB aabb) {
        if (this._player.collisionBox.intersects(aabb)) {
            return false;
        }
        return true;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        if (screen != null) {
            screen.init(this);
        }
    }

    //or maybe "render"?
    private void initGameContent(){
        LogoLoadingScreen logoLoadingScreen=new LogoLoadingScreen();
        this.setScreen(logoLoadingScreen);

        logHandler.info("registering blocks...");
        new Thread(() -> {
            BlockMap.registerBlockBehavior();
            BlockMap.registerBlock();
        }).start();
        while (BlockMap.getStatus()<1){
            logoLoadingScreen.updateProgress(0.3f*BlockMap.getStatus());
        }

        this.logHandler.info("loading textures...");
        this.logHandler.checkGLError("pre_font_load");
        for (int i=0;i<256;i++) {
            if(i >= 241 && i <= 248 || i >= 216 && i <= 239 || i == 8){
                continue;
            }
            FontRenderer.textures[i]=new Texture2D(false,false);
            FontRenderer.textures[i].generateTexture();
            String s2=Integer.toHexString(i);
            if(s2.length()==1){
                s2="0"+Integer.toHexString(i);
            }
            FontRenderer.textures[i].load("/resource/textures/font/unicode_page_" + s2+".png");
            logoLoadingScreen.updateProgress(0.3f+(i/255f)*0.7f);
        }
        this.logHandler.checkGLError("post_font_load");
    }

    private DisplayScreenInfo getDisplaySize() {
        int scale=GameSetting.instance.GUIScale;
        return new DisplayScreenInfo(
                scale,
                Math.max(Display.getWidth()/ scale,1),
                Math.max(Display.getHeight()/scale,1),
                Math.max(Display.getWidth()/scale,1)/2,
                Math.max(Display.getHeight()/scale,1)/2
        );
    }

    private void initDisplay() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(
                Integer.parseInt((String) Start.getArgs("width", "1280")),
                Integer.parseInt((String) Start.getArgs("height", "720"))));
        Display.setTitle((String) Start.getArgs("title", "CubeCraft-" + VERSION));
        Display.setResizable(true);
        Display.create();
        Keyboard.create();
        Mouse.create();
        Display.setFullscreen(Boolean.getBoolean((String) Start.getArgs("fullScreen", "false")));
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES,GameSetting.instance.FXAA);
        //Display.setIcon(ResourceUtil.getImageBufferFromStream(ResourcePacks.instance.getImage("/resources/textures/gui/logo2.png")));
        getDisplaySize();
    }
}
