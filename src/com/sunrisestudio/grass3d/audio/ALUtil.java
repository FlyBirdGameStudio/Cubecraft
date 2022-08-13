package com.sunrisestudio.grass3d.audio;

import org.lwjgl.openal.ALC11;

public class ALUtil {
    public static String[] getSpeakerDevices(){
        ALC11.alcGetString(0,ALC11.ALC_ALL_DEVICES_SPECIFIER);

        return null;
    }

    public static String getDefaultSpeakerDevice(){
        return ALC11.alcGetString(0,ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
    }


    public static String[] getCaptureDevices(){
        ALC11.alcGetString(0,ALC11.ALC_CAPTURE_DEVICE_SPECIFIER);

       return null;
    }

    public static String getDefaultCaptureDevice(){
        return ALC11.alcGetString(0,ALC11.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
    }
}
