package me.rina.rocan.client.module.client;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;

/**
 * @author SrRina
 * @since 07/12/20 at 12:48pm
 */
public class ModuleClickGUI extends Module {
    public static Setting width = new Setting("Width", "Width", "Scale width of the GUI.", 75, 75, 150);

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

        //if ((Integer) width.getValue() != Rocan.getModuleClickGUI().getMotherFrame().getScaleWidth()) {
        //    Rocan.getModuleClickGUI().getMotherFrame().setNewScaleWidth((Integer) width.getValue());
        //}
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen == Rocan.getModuleClickGUI()) {
            Rocan.getModuleClickGUI().setOpened(false);
        }
    }
}
