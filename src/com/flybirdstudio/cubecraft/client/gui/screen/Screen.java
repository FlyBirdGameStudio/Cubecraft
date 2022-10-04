package com.flybirdstudio.cubecraft.client.gui.screen;

import com.flybirdstudio.cubecraft.client.Cubecraft;
import com.flybirdstudio.cubecraft.client.gui.DisplayScreenInfo;
import com.flybirdstudio.cubecraft.GameSetting;
import com.flybirdstudio.cubecraft.client.gui.ScreenUtil;
import com.flybirdstudio.cubecraft.client.gui.component.Component;
import com.flybirdstudio.cubecraft.client.gui.Popup;
import com.flybirdstudio.cubecraft.client.resources.ResourceManager;
import com.flybirdstudio.starfish3d.platform.Display;
import com.flybirdstudio.starfish3d.platform.input.Keyboard;
import com.flybirdstudio.starfish3d.platform.input.Mouse;
import com.flybirdstudio.starfish3d.platform.input.InputHandler;
import com.flybirdstudio.starfish3d.platform.input.KeyboardCallback;
import com.flybirdstudio.starfish3d.platform.input.MouseCallBack;
import com.flybirdstudio.util.container.CollectionUtil;
import com.flybirdstudio.util.container.MultiMap;
import com.flybirdstudio.util.file.faml.FAMLDeserializer;
import com.flybirdstudio.util.file.faml.XmlReader;
import com.google.gson.*;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;



import java.lang.reflect.Type;

public class Screen {
    public final String id;
    public final ScreenType type;
    private Cubecraft platform;
    protected MultiMap<String,Component> components=new MultiMap<>();
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

    public void init(Cubecraft cubeCraft) {
        this.platform = cubeCraft;
        this.init();
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_default",this.getKeyboardCallback());
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_default",this.getMouseCallback());
        InputHandler.registerGlobalKeyboardCallback("cubecraft:scr_callback_base",new KeyboardCallback(){
            @Override
            public void onKeyEventPressed() {
                if(Keyboard.getEventKey()==Keyboard.KEY_F9){
                    ScreenUtil.createPopup("reloading...","reloading...",40,Popup.INFO);
                    ResourceManager.instance.reload(cubeCraft);
                    Screen.this.init();
                    ScreenUtil.createPopup("reload success","fully reloaded.",40,Popup.SUCCESS);
                }
            }
        });
        InputHandler.registerGlobalMouseCallback("cubecraft:scr_callback_base",new MouseCallBack(){
            @Override
            public void onButtonClicked(int eventButton) {
                if(eventButton==0){
                    int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
                    CollectionUtil.iterateMap(Screen.this.components, (key, item) -> item.onClicked(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale));
                }
            }
        });
    }

    public void render(DisplayScreenInfo info,float interpolationTime) {
        switch (this.type){
            case IMAGE_BACKGROUND -> ScreenUtil.renderPictureBackground();
            case TILE_BACKGROUND -> ScreenUtil.renderTileBackground();
            case IN_GAME -> ScreenUtil.renderMask();
        }

        CollectionUtil.iterateMap(this.components, (key, item) -> {
            GL11.glPushMatrix();
            item.render();
            GL11.glPopMatrix();
        });
        ScreenUtil.renderPopup(info,interpolationTime);
    }

    public void tick() {
        CollectionUtil.iterateMap(this.components, (key, item)-> {
            int scale=GameSetting.instance.getValueAsInt("client.gui.scale",2);
            item.resize(Display.getWidth()/ scale,Display.getHeight()/scale);
            item.tick(Mouse.getX()/ scale,(-Mouse.getY()+Display.getHeight())/scale);
        });
        ScreenUtil.tickPopup();
    }

    public boolean isInGameGUI(){
        return type==ScreenType.IN_GAME;
    }

    public MouseCallBack getMouseCallback(){
        return new MouseCallBack(){};
    }

    public KeyboardCallback getKeyboardCallback(){
        return new KeyboardCallback() {};
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
            return screen;
        }

        void deserializeComponentByType(String type,Element element,Screen target,XmlReader ctx){
            for (int i = 0; i < element.getElementsByTagName(type).getLength(); i++) {
                Element comp = (Element) element.getElementsByTagName(type).item(i);
                Component component = ctx.deserialize(comp, Component.getClass(type));
                target.components.put(comp.getAttribute("id"), component);
                component.setParent(target);
            }
        }
    }

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
                                Component.getClass(elements.get(i).getAsJsonObject().get("type").getAsString())
                        )
                );
            }
            return screen;
        }
    }

    public enum ScreenType{
        EMPTY,
        IN_GAME,
        IMAGE_BACKGROUND,
        TILE_BACKGROUND;

        public static ScreenType from(String id) {
            return switch (id){
                case "in-game"->IN_GAME;
                case "image-bg"->IMAGE_BACKGROUND;
                case "tile-bg"->TILE_BACKGROUND;
                case "empty"->EMPTY;
                default -> throw new IllegalArgumentException("no matched constant named %s".formatted(id));
            };
        }
    }
}