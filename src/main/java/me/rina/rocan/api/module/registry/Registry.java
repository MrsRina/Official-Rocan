package me.rina.rocan.api.module.registry;

import me.rina.rocan.api.module.impl.ModuleCategory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author SrRina
 * @since 08/02/2021 at 11:50
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Registry {
    String name();
    String tag();
    String description() default "";

    ModuleCategory category();
}
