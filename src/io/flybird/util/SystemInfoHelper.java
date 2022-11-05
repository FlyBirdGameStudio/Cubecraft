package io.flybird.util;
import oshi.SystemInfo;
import oshi.hardware.*;

public class SystemInfoHelper {
    private static SystemInfo systemInfo;
    private static HardwareAbstractionLayer hardware;
    private static CentralProcessor cpu;
    private static GraphicsCard gpu;
    private static SoundCard soundCard;
    private static CentralProcessor.ProcessorIdentifier cpuID;
    private static GlobalMemory memory;

    public static void init(){
        SystemInfo systemInfo=new SystemInfo();
        hardware=systemInfo.getHardware();
        cpu=hardware.getProcessor();
        gpu=hardware.getGraphicsCards().get(0);
        soundCard=hardware.getSoundCards().get(0);
        cpuID=getCpu().getProcessorIdentifier();
        memory=hardware.getMemory();
    }

    public static CentralProcessor getCpu() {
        return cpu;
    }

    public static GraphicsCard getGpu() {
        return gpu;
    }

    public static SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public static HardwareAbstractionLayer getHardware() {
        return hardware;
    }

    public static SoundCard getSoundCard() {
        return soundCard;
    }

    public static int getCpuCores(){
        return getCpu().getPhysicalProcessorCount();
    }

    public static String getCpuName(){
        return getCpuID().getName();
    }

    public static boolean isCpu64bit(){
        return getCpuID().isCpu64bit();
    }

    public static CentralProcessor.ProcessorIdentifier getCpuID() {
        return cpuID;
    }

    public static String getGpuName(){
        return getGpu().getName();
    }

    public static long getGpuVRam(){
        return getGpu().getVRam();
    }

    public static long getMemInstalled(){
        return getMemory().getTotal();
    }

    public static GlobalMemory getMemory() {
        return memory;
    }

    public static String getSoundCardName() {
        return getSoundCard().getName();
    }
}
