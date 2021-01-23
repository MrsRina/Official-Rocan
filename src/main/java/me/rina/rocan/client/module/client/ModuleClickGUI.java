package me.rina.rocan.client.module.client;

import cat.yoink.eventmanager.Listener;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;

/**
 * @author SrRina
 * @since 07/12/20 at 12:48pm
 */
public class ModuleClickGUI extends Module {
    public static ValueBoolean closeAnimation = new ValueBoolean("Close Animation", "CloseAnimation", "Cool smooth effect when close GUI", true);
    public static ValueBoolean drawDefaultMinecraftBackground = new ValueBoolean("Draw Default Minecraft Background", "DrawDefaultMinecraftBackground", "Draw the default Minecraft background at GUI.", true);

    public static ValueString typeBox = new ValueString("Type Box", "TypeBox", "Black.", "SCrim").addFormat("fuck just type");

    public ModuleClickGUI() {
        super("Module Click GUI", "ModuleClickGUI", "Open GUI to manage module, settings...", ModuleCategory.Client);
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
