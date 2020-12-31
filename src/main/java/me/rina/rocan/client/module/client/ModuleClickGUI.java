package me.rina.rocan.client.module.client;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.event.client.ClientTickEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 07/12/20 at 12:48pm
 */
public class ModuleClickGUI extends Module {
    public static Setting button = new Setting("Button", "Button", "Simple button widget.", false);
    public static Setting slider = new Setting("Slider", "Slider", "Simple slider widget.", 5, 0, 10);
    public static Setting mode   = new Setting("Mode", "Mode", "Simple mode widget.", Mode.Option2);

    public ModuleClickGUI() {
        super("Module Click GUI", "ModuleClickGUI", "Open GUI to manage module, settings...", ModuleCategory.Client);
    }

    protected enum Mode {
        Option1, Option2;
    }

    @Listener
    public void onListenClientEvent(ClientTickEvent event) {
        if (mc.currentScreen != Rocan.getModuleClickGUI()) {
            mc.displayGuiScreen(Rocan.getModuleClickGUI());
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen == Rocan.getModuleClickGUI()) {
            Rocan.getModuleClickGUI().setOpened(false);
        }
    }
}
