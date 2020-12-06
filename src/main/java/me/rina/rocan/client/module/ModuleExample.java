package me.rina.rocan.client.module;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.event.client.ClientTickEvent;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public class ModuleExample extends Module {
    public static Setting button = new Setting("Button", "Button", "Button i dont know.", true);

    public ModuleExample() {
        super("Module Example", "ModuleExample", "Module example.", ModuleCategory.Client);
    }

    @Listener
    public void onClientTick(ClientTickEvent event) {
        // Work as update method.
    }
}
