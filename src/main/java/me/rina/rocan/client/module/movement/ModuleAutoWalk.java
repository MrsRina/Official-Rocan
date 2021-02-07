package me.rina.rocan.client.module.movement;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

public class ModuleAutoWalk extends Module {

    public ModuleAutoWalk() {
        super("Auto Walk","AutoWalk","Automatically walks", ModuleCategory.MOVEMENT);
    }

    @Listener
    public void onUpdate(ClientTickEvent event) {
        if (!mc.gameSettings.keyBindForward.isPressed())
            mc.gameSettings.keyBindForward.pressed = true;
        mc.gameSettings.keyBindForward.pressed = true;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }
}