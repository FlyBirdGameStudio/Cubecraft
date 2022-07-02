package com.SunriseStudio.cubecraft;

public class GameSetting {
    public static GameSetting instance=new GameSetting();

    //render
    public int renderDistance =6;
    public int renderLoadingDistance=8;
    //display
    public int GUIScale=2;
    public int maxUpdatesPreFrame=1;

    public int FXAA=8;
    public String gamePath="C://openMC";
    public int maxDrawThread=4;
}
