package com.sunrisestudio.grass3d.audio;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.*;

import java.nio.IntBuffer;

public class Audio {
    private static long device;
    private static long context;

    public static void create(){
        device =ALC10.alcOpenDevice(ALUtil.getDefaultSpeakerDevice());

        ALC.createCapabilities(device);
        context=ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);

    }

    public static void destroy(){
        ALC10.alcCloseDevice(device);
        ALC10.alcDestroyContext(context);
    }



}
