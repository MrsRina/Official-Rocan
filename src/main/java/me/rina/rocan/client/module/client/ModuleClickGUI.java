package me.rina.rocan.client.module.client;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.event.client.ClientTickEvent;

/**
 * @author SrRina
 * @since 07/12/20 at 12:48pm
 */
public class ModuleClickGUI extends Module {
    public ModuleClickGUI() {
        super("Module Click GUI", "ModuleClickGUI", "Open GUI to manage module, settings...", ModuleCategory.Client);
    }

    @Listener
    public void onListenClientEvent(ClientTickEvent event) {
        if (mc.currentScreen == null) {
            mc.displayGuiScreen(Rocan.getModuleClickGUI());

            setDisabled();
        }
    }
}
