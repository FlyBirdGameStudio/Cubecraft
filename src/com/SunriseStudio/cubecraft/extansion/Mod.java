package com.sunrisestudio.cubecraft.extansion;

public abstract class Mod {
    private final PlatformClient client;
    private final ExtansionRunningTarget targetPlatform;
    private final PlatformServer server;
    private PlatformBase platform;

    public Mod(
            PlatformClient client,
            PlatformServer server,
            PlatformBase platform,
            ExtansionRunningTarget target
    ){
        this.client=client;
        this.server=server;
        this.targetPlatform=target;
        this.platform=platform;
    }

    public abstract void construct();

    public PlatformClient getClient() {
        return client;
    }

    public PlatformServer getServer() {
        return server;
    }

    public ExtansionRunningTarget getTargetPlatform() {
        return targetPlatform;
    }

    public PlatformBase getPlatformBase() {
        return this.platform;
    }
}
