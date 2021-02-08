package me.rina.rocan.client.module.render;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;

/**
 * @author SrRina
 * @since 07/02/2021 at 15:47
 **/
public class ModuleNameTags extends Module {
    public ModuleNameTags() {
        super("Name Tags", "NameTags", "Better name tag player.", ModuleCategory.RENDER);
    }
}