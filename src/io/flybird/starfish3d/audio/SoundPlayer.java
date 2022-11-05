package io.flybird.starfish3d.audio;

import org.lwjgl.openal.AL10;

import java.util.ArrayList;

public class SoundPlayer {
    private SoundListenerPosition soundListenerPosition;
    private ArrayList<Sound> sounds=new ArrayList();



    public void setSoundListenerPosition(SoundListenerPosition soundListenerPosition) {
        this.soundListenerPosition = soundListenerPosition;
    }

    public void update(){
        for (Sound s:this.sounds){
            AL10.alSourcefv(s.alSource,AL10.AL_POSITION,new float[]{
                    (float) (s.getPosition().x-this.soundListenerPosition.x()),
                    (float) (s.getPosition().y-this.soundListenerPosition.y()),
                    (float) (s.getPosition().z-this.soundListenerPosition.z()),
            });

        }
    }
}
