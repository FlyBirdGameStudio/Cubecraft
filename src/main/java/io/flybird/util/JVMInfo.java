package io.flybird.util;

import java.lang.management.ManagementFactory;

public class JVMInfo {
    /**
     * get java version
     * @return java version
     */
    public static String getJavaVersion(){
        return ManagementFactory.getRuntimeMXBean().getVmVersion();
    }

    public static String getJavaName(){
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public static String getOSName(){
        return ManagementFactory.getOperatingSystemMXBean().getName();
    }

    public static String getOSVersion(){
        return ManagementFactory.getOperatingSystemMXBean().getVersion();
    }

    public static String getUsedMemory(){
        Runtime runtime=Runtime.getRuntime();
        return (runtime.totalMemory()-runtime.freeMemory())/1048576+"M";
    }

    public static String getTotalMemory(){
        Runtime runtime=Runtime.getRuntime();
        return (runtime.totalMemory())/1048576+"M";
    }

    public static String getUsage(){
        Runtime runtime=Runtime.getRuntime();
        return (int)(100-(runtime.freeMemory()/ (float)runtime.totalMemory())*100)+"%";
    }
}
