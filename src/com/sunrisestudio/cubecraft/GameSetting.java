package com.sunrisestudio.cubecraft;

public class GameSetting {
    public static GameSetting instance=new GameSetting();

    //render
    public int renderDistance =2;
    public int renderLoadingDistance=4;
    //display
    public int GUIScale=2;
    public int maxUpdatesPreFrame=1;

    public int FXAA=4;
    public String gamePath="C://openMC";
    public int maxDrawThread=maxUpdatesPreFrame;
}
