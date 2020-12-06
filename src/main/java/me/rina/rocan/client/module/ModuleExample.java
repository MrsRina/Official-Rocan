package me.rina.rocan.client.module;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class ModuleExample extends Module {
    public static Setting button = new Setting("Button", "Button", "Button i dont know.", true);

    public ModuleExample() {
        super("Module Example", "ModuleExample", "Module example.", ModuleCategory.Client);
    }

    @Override
    public void onUpdate() {
    }
}
