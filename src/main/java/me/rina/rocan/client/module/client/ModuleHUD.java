package me.rina.rocan.client.module.client;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.client.event.client.ClientTickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 27/03/2021 at 00:13
 **/
@Registry(name = "HUD", tag = "HUD", description = "Draws overlay component of client HUD.", category = ModuleCategory.CLIENT)
public class ModuleHUD extends Module {
    /* Misc. */
    public static ValueBoolean settingRender = new ValueBoolean("Render", "Render", "Render HUDs components.", true);

    /* Color. */
    public static ValueNumber settingRed = new ValueNumber("Red", "Red", "Color string.", 255, 0, 255);
    public static ValueNumber settingGreen = new ValueNumber("Green", "Green", "Color string.", 255, 0, 255);
    public static ValueNumber settingBlue = new ValueNumber("Blue", "Blue", "Color string.", 0, 0, 255);

    @Listener
    public void onListen(ClientTickEvent event) {
        // Its my brain.
        if (mc.currentScreen != Rocan.getComponentClickGUI()) {
            mc.displayGuiScreen(Rocan.getComponentClickGUI());
        }
    }

    @Override
    public void onDisable() {
        // Its my brain.
        if (mc.currentScreen == Rocan.getModuleClickGUI()) {
            Rocan.getComponentClickGUI().setState(false);
        }
    }
}
