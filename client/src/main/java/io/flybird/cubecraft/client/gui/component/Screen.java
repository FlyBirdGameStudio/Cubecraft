package io.flybird.cubecraft.client.gui.component;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.ScreenUtil;
import io.flybird.cubecraft.client.gui.base.DisplayScreenInfo;
import io.flybird.cubecraft.client.gui.font.FontAlignment;

import io.flybird.cubecraft.client.gui.font.SmoothedFontRenderer;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.util.JVMInfo;
import io.flybird.util.container.*;
import io.flybird.util.file.FAMLDeserializer;
import io.flybird.util.file.XmlReader;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

public class Screen extends Container {
    protected final String[] debugInfoLeft = new String[64];
    protected final String[] debugInfoRight = new String[64];

    protected final boolean grabMouse;
    public final String id;
    public final ScreenType type;
    protected Cubecraft platform;
    protected final MultiMap<String, Component> components=new MultiMap<>();
    private Screen parent;

    //init
    public Screen(boolean grabMouse, String id, ScreenType type){
        this.grabMouse = grabMouse;
        this.id = id;
        this.type = type;
    }



    /**<h3>init a screen here,runs when screen initialize.</h3>
     * This method need to overwrite.
     * you could setup every thing here.
     */
    public void init(){}

    public void init(Cubecraft cubecraft) {
        this.platform = cubecraft;
        this.init();
        this.initFixedDebugInfo();
        CollectionUtil.iterateMap(this.components,((key, item) -> this.platform.getWindow().getEventBus().registerEventListener(item)));
        this.platform.getWindow().getEventBus().registerEventListener(this);
        this.platform.getWindow().setMouseGrabbed(this.grabMouse);
    }

    //debug
    public void renderDebug(DisplayScreenInfo info){
        int pos = 2;
        for (String s : this.debugInfoLeft) {
            ClientRegistries.SMOOTH_FONT_RENDERER.renderShadow(s, 2, pos, 16777215, 8, FontAlignment.LEFT);
            pos += 10;
        }
        pos=2;
        for (String s : this.debugInfoRight) {
            ClientRegistries.SMOOTH_FONT_RENDERER.renderShadow(s, info.scrWidth()-2, pos, 16777215, 8, FontAlignment.RIGHT);
            pos += 10;
        }
    }

    public void getDebugInfoTick(){
        this.debugInfoLeft[2] = "ClientTPS：" + this.getPlatform().getTimingInfo().longTickTPS() + "/MSPT:" + this.getPlatform().getTimingInfo().longTickMSPT();
        if(this.parent!=null){
            this.debugInfoLeft[4]="CurrentGUI：%s(%s)=[%s]".formatted(this.getID(), this.parent.getID(),this.getClass().getName());
        }else{
            this.debugInfoLeft[4]="CurrentGUI：%s(null)=[%s]".formatted(this.getID(),this.getClass().getName());
        }
        this.debugInfoLeft[5]="MousePosition:%d/%d".formatted(this.platform.getWindow().getMouseX(),this.platform.getWindow().getMouseY());

        this.debugInfoRight[1] = "Memory(jvm)(Used/All)：%s/%s(%s)".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage());

        //this.debugInfoLeft[3]="AliasFontRenderer:%s(cached%d)".formatted(SmoothedFontRenderer.getFontFamily().getName(),SmoothedFontRenderer.getCacheSize());
    }

    public void getDebugInfoFrame(){
        this.debugInfoLeft[1] = "FPS:%d/FrameTime:%d(%dVertexUpload)".formatted(
                this.getPlatform().getTimingInfo().shortTickTPS(),
                this.getPlatform().getTimingInfo().shortTickMSPT(),
                VertexArrayUploader.getUploadedCount()
        );
        VertexArrayUploader.resetUploadCount();
    }

    public void initFixedDebugInfo(){
        this.debugInfoLeft[0] = "Cubecraft-" + Cubecraft.VERSION;

        this.debugInfoRight[0] = "JVM：" + JVMInfo.getJavaName() + "/" + JVMInfo.getJavaVersion();
        this.debugInfoRight[2]="openGL:"+ GL11.glGetString(GL11.GL_VERSION);
        //this.debugInfoRight[3]="openAL:"+ AL11.alGetString(AL11.AL_VERSION);
        this.debugInfoRight[4] = "OS:%s-%s".formatted(JVMInfo.getOSName(), JVMInfo.getOSVersion());
    }


    //run
    public void render(DisplayScreenInfo info, float interpolationTime) {
        switch (this.type){
            case IMAGE_BACKGROUND -> ScreenUtil.renderPictureBackground(this.platform.getWindow());
            case TILE_BACKGROUND -> ScreenUtil.renderTileBackground();
            case IN_GAME -> ScreenUtil.renderMask(this.platform.getWindow());
            case IMAGE_BLUR_BACKGROUND -> ScreenUtil.renderPictureBackgroundBlur(this.platform.getWindow());
            case IMAGE_BLUR_MASK_BACKGROUND -> {
                ScreenUtil.renderPictureBackgroundBlur(this.platform.getWindow());
                ScreenUtil.renderMask(this.platform.getWindow());
            }
        }

        CollectionUtil.iterateMap(this.components, (key, item) -> {
            GL11.glPushMatrix();
            item.render();
            GL11.glPopMatrix();
        });

        if(this.getPlatform().isDebug) {
            this.getDebugInfoFrame();
            this.renderDebug(info);
        }
    }

    public void tick() {
        this.getDebugInfoTick();
        CollectionUtil.iterateMap(this.components, (key, item)-> {
            int scale=this.platform.getGameSetting().getValueAsInt("client.render.gui.scale",2);
            item.resize(this.platform.getWindow().getWindowWidth()/ scale,this.platform.getWindow().getWindowHeight()/scale);
            item.tick();
        });

    }


    //attribute
    public boolean isInGameGUI(){
        return type==ScreenType.IN_GAME;
    }

    public Screen getParentScreen() {
        return this.parent;
    }

    public Cubecraft getPlatform() {
        return platform;
    }

    public String getID() {
        return id;
    }

    public MultiMap<String,Component> getComponents() {
        return this.components;
    }

    public void setParentScreen(Screen scr) {
        this.parent=scr;
    }

    public void release(){
        CollectionUtil.iterateMap(this.components,((key, item) -> Screen.this.platform.getWindow().getEventBus().unregisterEventListener(item)));
        Screen.this.platform.getWindow().getEventBus().unregisterEventListener(this);
    }


    //decode
    public static class XMLDeserializer implements FAMLDeserializer<Screen> {
        @Override
        public Screen deserialize(Element element, XmlReader famlLoadingContext) {
            Element meta = (Element) element.getElementsByTagName("meta").item(0);

            String id=meta.getElementsByTagName("id").item(0).getTextContent();
            String type=meta.getElementsByTagName("type").item(0).getTextContent();

            boolean grab=false;
            if(meta.getElementsByTagName("grab").getLength()>0){
                grab=Boolean.parseBoolean(meta.getElementsByTagName("grab").item(0).getTextContent());
            }
            Screen screen = new Screen(grab, id,ScreenType.from(type));

            for (String s: ClientRegistries.GUI_MANAGER.getCompIdList()){
                this.deserializeComponentByType(s,element,screen,famlLoadingContext);
            }
            return screen;
        }

        void deserializeComponentByType(String type,Element element,Screen target,XmlReader ctx){
            for (int i = 0; i < element.getElementsByTagName(type).getLength(); i++) {
                Element comp = (Element) element.getElementsByTagName(type).item(i);
                Component component = ctx.deserialize(comp, ClientRegistries.GUI_MANAGER.getCompClass(type));
                target.components.put(comp.getAttribute("id"), component);
                component.setParent(target);
            }
        }
    }

    public enum ScreenType{
        EMPTY,
        IN_GAME,
        IMAGE_BACKGROUND,
        IMAGE_BLUR_BACKGROUND,
        TILE_BACKGROUND,
        IMAGE_BLUR_MASK_BACKGROUND;

        public static ScreenType from(String id) {
            return switch (id){
                case "in-game"->IN_GAME;
                case "image-bg"->IMAGE_BACKGROUND;
                case "tile-bg"->TILE_BACKGROUND;
                case "image-bg-blur"->IMAGE_BLUR_BACKGROUND;
                case "image-bg-blur-mask"->IMAGE_BLUR_MASK_BACKGROUND;
                case "empty"->EMPTY;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(id));
            };
        }
    }
}