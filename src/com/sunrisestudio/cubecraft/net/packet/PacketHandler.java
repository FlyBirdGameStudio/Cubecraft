package com.sunrisestudio.cubecraft.net.packet;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited

public @interface PacketHandler {
    String handledType();
}
