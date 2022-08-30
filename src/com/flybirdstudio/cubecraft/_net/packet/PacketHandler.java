package com.flybirdstudio.cubecraft.client.net.packet;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited

public @interface PacketHandler {
    String handledType();
}
