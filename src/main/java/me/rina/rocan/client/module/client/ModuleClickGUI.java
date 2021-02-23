package me.rina.rocan.client.module.client;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.client.event.client.ClientTickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 07/12/20 at 12:48pm
 */
@Registry(name = "Click GUI", tag = "ClickGUI", description = "Open GUI to manage module, settings...", category =  ModuleCategory.CLIENT)
public class ModuleClickGUI extends Module {
    public static ValueBoolean closeAnimation = new ValueBoolean("Close Animation", "CloseAnimation", "Cool smooth effect when close GUI", true);
    public static ValueBoolean drawDefaultMinecraftBackground = new ValueBoolean("Draw Default Minecraft Background", "DrawDefaultMinecraftBackground", "Draw the default Minecraft background at GUI.", true);

    @Listener
    public void onListen(ClientTickEvent event) {
        // Its my brain.
        if (mc.currentScreen != Rocan.getModuleClickGUI()) {
            mc.displayGuiScreen(Rocan.getModuleClickGUI());
        }
    }

    @Override
    public void onDisable() {
        // Its my brain.
        if (mc.currentScreen == Rocan.getModuleClickGUI()) {
            Rocan.getModuleClickGUI().setOpened(false);
        }
    }
}
