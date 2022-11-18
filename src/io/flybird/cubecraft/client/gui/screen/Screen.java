package io.flybird.cubecraft.client.gui.screen;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.gui.*;
import io.flybird.cubecraft.GameSetting;
import io.flybird.cubecraft.client.gui.component.*;
import io.flybird.cubecraft.client.gui.component.control.Button;
import io.flybird.cubecraft.client.gui.component.control.TextBar;
import io.flybird.cubecraft.resources.ResourceManager;
import io.flybird.starfish3d.platform.Display;
import io.flybird.starfish3d.event.KeyPressEvent;
import io.flybird.starfish3d.platform.input.*;
import io.flybird.starfish3d.render.draw.VertexArrayUploader;
import io.flybird.util.JVMInfo;
import io.flybird.util.container.*;
import io.flybird.util.event.EventHandler;
import io.flybird.util.file.faml.*;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

public class Screen extends Container {
    protected final String[] debugInfoLeft = new String[64];
    protected final String[] debugInfoRight = new String[64];

    public final String id;
    public final ScreenType type;
    private Cubecraft platform;
    protected MultiMap<String, Component> components=new MultiMap<>();
    private Screen parent;

    //init
    public Screen(String id, ScreenType type){
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
        CollectionUtil.iterateMap(this.components,((key, item) -> Display.getEventBus().registerEventListener(item)));
        Display.getEventBus().registerEventListener(this);

        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_default",this.getMouseCallback());
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_base",new MouseCallBack(){
            @Override
            public void onButtonClicked(int eventButton) {
                if(eventButton==0){
                    int scale= GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
                    CollectionUtil.iterateMap(Screen.this.components, (key, item) -> item.onClicked(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale));
                }
            }
        });
    }

    @EventHandler
    public void onKeyPressed(KeyPressEvent e){
        if(e.key()==Keyboard.KEY_F9){
            ScreenUtil.createPopup("reloading...","reloading...",40,Popup.INFO);
            ResourceManager.instance.reload(this.platform);
            Screen.this.init();
            ScreenUtil.createPopup("reload success","fully reloaded.",40,Popup.SUCCESS);
        }
        if(e.key()==Keyboard.KEY_F3){
            Screen.this.getPlatform().isDebug=!Screen.this.getPlatform().isDebug;
        }
    }

    //debug
    public void renderDebug(DisplayScreenInfo info){
        int pos = 2;
        for (String s : this.debugInfoLeft) {
            FontRenderer.renderShadow(s, 2, pos, 16777215, 8, FontAlignment.LEFT);
            pos += 10;
        }
        pos=2;
        for (String s : this.debugInfoRight) {
            FontRenderer.renderShadow(s, info.scrWidth()-2, pos, 16777215, 8, FontAlignment.RIGHT);
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
        this.debugInfoLeft[5]="MousePosition:%d/%d".formatted(Mouse.getX(),Mouse.getY());

        this.debugInfoRight[1] = "Memory(jvm)(Used/All)：%s/%s(%s)".formatted(JVMInfo.getUsedMemory(), JVMInfo.getTotalMemory(), JVMInfo.getUsage());
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
        this.debugInfoRight[3]="openAL:"+ AL11.alGetString(AL11.AL_VERSION);
        this.debugInfoRight[4] = "OS:%s-%s".formatted(JVMInfo.getOSName(), JVMInfo.getOSVersion());
    }


    //run
    public void render(DisplayScreenInfo info, float interpolationTime) {
        switch (this.type){
            case IMAGE_BACKGROUND -> ScreenUtil.renderPictureBackground();
            case TILE_BACKGROUND -> ScreenUtil.renderTileBackground();
            case IN_GAME -> ScreenUtil.renderMask();
            case IMAGE_BLUR_BACKGROUND -> ScreenUtil.renderPictureBackgroundBlur();
            case IMAGE_BLUR_MASK_BACKGROUND -> {
                ScreenUtil.renderPictureBackgroundBlur();
                ScreenUtil.renderMask();
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
            int scale=GameSetting.instance.getValueAsInt("client.render.gui.scale",2);
            item.resize(Display.getWidth()/ scale,Display.getHeight()/scale);
            item.tick();
        });

    }


    //attribute
    public boolean isInGameGUI(){
        return type==ScreenType.IN_GAME;
    }

    public MouseCallBack getMouseCallback(){
        return new MouseCallBack(){};
    }

    public Screen getParentScreen() {
        return this.parent;
    }

    protected void addComponent(Component component) {
        this.components.put(component.toString(),component);
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


    //decode
    public static class XMLDeserializer implements FAMLDeserializer<Screen> {
        @Override
        public Screen deserialize(Element element, XmlReader famlLoadingContext) {
            Element meta = (Element) element.getElementsByTagName("meta").item(0);

            String id=meta.getElementsByTagName("id").item(0).getTextContent();
            String type=meta.getElementsByTagName("type").item(0).getTextContent();
            Screen screen = new Screen(id,ScreenType.from(type));

            this.deserializeComponentByType("button",element,screen,famlLoadingContext);
            this.deserializeComponentByType("label",element,screen,famlLoadingContext);
            this.deserializeComponentByType("image",element,screen,famlLoadingContext);
            this.deserializeComponentByType("splash",element,screen,famlLoadingContext);
            this.deserializeComponentByType("topbar",element,screen,famlLoadingContext);
            this.deserializeComponentByType("panel",element,screen,famlLoadingContext);
            this.deserializeComponentByType("textbar",element,screen,famlLoadingContext);
            return screen;
        }

        Class<? extends Component> getClass(String name){
            return switch (name){
                case "button"-> Button.class;
                case "label"->Label.class;
                case "image"->ImageRenderer.class;
                case "splash"->SplashText.class;
                case "topbar"->TopBar.class;
                case "panel"->Panel.class;
                case "textbar"-> TextBar.class;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(name));
            };
        }

        void deserializeComponentByType(String type,Element element,Screen target,XmlReader ctx){
            for (int i = 0; i < element.getElementsByTagName(type).getLength(); i++) {
                Element comp = (Element) element.getElementsByTagName(type).item(i);
                Component component = ctx.deserialize(comp, getClass(type));
                target.components.put(comp.getAttribute("id"), component);
                component.setParent(target);
            }
        }
    }
/*
    public static class JDeserializer implements JsonDeserializer<Screen>{
        @Override
        public Screen deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject scr=jsonElement.getAsJsonObject();
            JsonObject meta=scr.get("meta").getAsJsonObject();
            Screen screen= new Screen(
                    meta.get("id").getAsString(),
                    ScreenType.from(meta.get("type").getAsString())
            );
            JsonArray elements=scr.get("elements").getAsJsonArray();
            for (int i = 0; i < elements.size(); i++) {
                screen.getComponents().put(
                        elements.get(i).getAsJsonObject().get("id").getAsString(),
                        jsonDeserializationContext.deserialize(
                                elements.get(i),
                                getClass(elements.get(i).getAsJsonObject().get("type").getAsString())
                        )
                );
            }
            return screen;
        }
    }
*/
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