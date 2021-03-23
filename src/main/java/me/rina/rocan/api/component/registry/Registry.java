package me.rina.rocan.api.component.registry;

import me.rina.rocan.api.component.impl.ComponentType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author SrRina
 * @since 23/03/2021 at 15:23
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Registry {
    String name();
    String tag();
    String description() default "";

    ComponentType type() default ComponentType.TEXT;
}
