package io.flybird.cubecraft.extansion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * manifest of a mod.
 *
 * @author GrassBlock2022
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CubecraftMod {
    String name();
    String version();
}