package com.SunriseStudio.cubecraft.world.block.registery.block;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BlockGetter{
    String id();
    String behavior();
}
